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
	docker run --rm --name ${CONTAINER_NAME} -p ${LOCAL_PORT}:${CONTAINER_PORT}  -p 8443:8443  erasmolpa/github-client:${TAG}

push: ## Docker Push run default ports
	docker push erasmolpa/github-client:${TAG}

dc-stop: ## Stop all containers.
	docker stop $(docker ps -q)

dc-kill: ## Kill all containers.
	 docker kill $(docker ps -q)

dc-rm: ## Removes all containers.
	docker rm $(docker ps -a -q)

dc-rmi: ## Removes all images.
	docker rmi $(docker images -a -q)

dc-rmdi: ## Removes dangling images.
	docker rmi $(docker images -a --filter=dangling=true -q)

dc-rm-exited-containers: ## Removes containers that are done.
	docker rm -v $(docker ps -a -q -f status=exited)

dc-prune: dc-kill ## Removes containers that are done.
	docker system prune

service-up-from-scratch: mv-install service-restart ## Recreate artifact, docker image and redeploy de Compose

service-up: ## Docker Compose Start all or c=<name> containers in foreground
	${compose} up -d --force-recreate --build $(c)

service-start: ## Docker Compose Start all or c=<name> containers in background
	${compose} up -d $(c)

service-stop: ## Docker Compose Stop all or c=<name> containers
	${compose} stop $(c)

service-restart: service-down ## Docker Compose Restart all or c=<name> containers
	${compose} up -d --force-recreate --build $(c)

service-logs: ## Docker Compose Show logs for all or c=<name> containers
	${compose} logs --tail=100 -f $(c)

service-down:  ## Docker Compose Clean all data
	${compose} down

service-status: ## Docker Compose Show status of containers
	${compose} ps

ps: status ## Alias of status


