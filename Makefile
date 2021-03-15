include .env

compose=docker-compose
config= --env-file .env

help:
	@awk 'BEGIN {FS = ":.*?## "} /^[a-zA-Z_-]+:.*?## / {printf "\033[36m%-15s\033[0m %s\n", $$1, $$2}' $(MAKEFILE_LIST)

mv-install: ## Maven Package
	mvn clean install

mv-build: ## Maven Build Docker image using maven build plugin
   mvn spring-boot:build-image

build: ## Docker Build image using maven build plugin
	docker build  --no-cache -t erasmolpa/github-client:${TAG} .

scan: ## Docker Scan Dockerfile
	docker scan --file Dockerfile

run: ## Docker Run default ports
	docker run -p ${LOCAL_PORT}:${CONTAINER_PORT}  -p 8443:8443  erasmolpa/github-client:${TAG}

push: ## Docker Push run default ports
	docker push erasmolpa/github-client:${TAG}


up: ## Docker Compose Start all or c=<name> containers in foreground
	${compose} up -d --force-recreate --build $(c)

start: ## Docker Compose Start all or c=<name> containers in background
	${compose} up -d $(c)

stop: ## Docker Compose Stop all or c=<name> containers
	${compose} stop $(c)

restart: ## Docker Compose Restart all or c=<name> containers
	${compose} stop $(c)
	${compose} up -d --force-recreate --build $(c)

logs: ## Docker Compose Show logs for all or c=<name> containers
	${compose} logs --tail=100 -f $(c)

status: ## Docker Compose Show status of containers
	${compose} ps

ps: status ## Alias of status

down:  ## Docker Compose Clean all data
	${compose} down
