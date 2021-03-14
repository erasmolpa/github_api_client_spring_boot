include .env

compose=docker-compose
config= --env-file .env
help:
	@awk 'BEGIN {FS = ":.*?## "} /^[a-zA-Z_-]+:.*?## / {printf "\033[36m%-15s\033[0m %s\n", $$1, $$2}' $(MAKEFILE_LIST)

mv-install: ## Maven Package
	mvn clean install

mv-build: ## Build Docker image using maven build plugin
   mvn spring-boot:build-image

build: ## Docker Build image using maven build plugin
	docker build -t erasmolpa/github-client:${TAG} .

run: ## Docker Run default ports
	docker run -p ${LOCAL_PORT}:${CONTAINER_PORT}  -p 8443:8443  erasmolpa/github-client:${TAG}

push: ## Docker Push run default ports
	docker push erasmolpa/github-client:${TAG}


up: ## Start all or c=<name> containers in foreground
	${compose} up  --force-recreate  $(c) -d

start: ## Start all or c=<name> containers in background
	${compose} up -d $(c)

stop: ## Stop all or c=<name> containers
	${compose} stop $(c)

restart: ## Restart all or c=<name> containers
	${compose} stop $(c)
	${compose} up  --force-recreate  $(c) -d

logs: ## Show logs for all or c=<name> containers
	${compose} logs --tail=100 -f $(c)

status: ## Show status of containers
	${compose} ps

ps: status ## Alias of status

down:  ## Clean all data
	${compose} down