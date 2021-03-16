include .env

compose=docker-compose
config= --env-file .env

PHONY: help
help:
	@awk 'BEGIN {FS = ":.*?## "} /^[a-zA-Z_-]+:.*?## / {printf "\033[36m%-15s\033[0m %s\n", $$1, $$2}' $(MAKEFILE_LIST)

m-install: ## MAVEN Package
	mvn clean install

m-build: ## MAVEN Build Docker image using maven build plugin
    mvn spring-boot:build-image

build-from-scratch: m-install d-build ## Maven install and Docker build

d-build: ## DOCKER Build image using maven build plugin
	docker build  --no-cache -t erasmolpa/github-client:${TAG} .

d-scan: ## DOCKER Scan Dockerfile
	docker scan --file Dockerfile

d-run: ## DOCKER Run default ports
	docker run --rm --name ${CONTAINER_NAME} -p ${LOCAL_PORT}:${CONTAINER_PORT}  -p 8443:8443  erasmolpa/github-client:${TAG}

d-push: ## DOCKER Push run default ports
	docker push erasmolpa/github-client:${TAG}

d-stop: ## DOCKER Stop all containers.
	docker stop $(docker ps -q)

d-kill: ## DOCKER Kill all containers.
	 docker kill $(docker ps -q)

d-rm: ## DOCKER Removes all containers.
	docker rm $(docker ps -a -q)

d-rmi: ## DOCKER Removes all images.
	docker rmi $(docker images -a -q)

d-rmdi: ## DOCKER Removes dangling images.
	docker rmi $(docker images -a --filter=dangling=true -q)

d-rm-exited-containers: ## DOCKER Removes containers that are done.
	docker rm -v $(docker ps -a -q -f status=exited)

d-prune: d-kill ## DOCKER Removes containers that are done.
	docker system prune

c-up-from-scratch: m-install c-restart ##  DOCKER COMPOSE Recreate artifact, docker image and redeploy de Compose

c-up: ##  DOCKER-COMPOSE Start all or c=<name> containers in foreground
	${compose} up -d --force-recreate --build $(c)

c-start: ##  DOCKER-COMPOSE Start all or c=<name> containers in background
	${compose} up -d $(c)

c-stop: ##  DOCKER-COMPOSE Stop all or c=<name> containers
	${compose} stop $(c)

c-restart: c-down ##  DOCKER-COMPOSE Restart all or c=<name> containers
	${compose} up -d --force-recreate --build $(c)

.PHONY: c-logs
c-logs: ##  DOCKER-COMPOSE Show logs for all or c=<name> containers
	${compose} logs --tail=100 -f $(c)

.PHONY: c-down
c-down:  ##  DOCKER-COMPOSE Clean all data
	${compose} down

.PHONY: c-status
c-status: ##  DOCKER-COMPOSE Show status of containers
	${compose} ps

.PHONY: k-info
k-info: ## HELM status show the current release status
	kubectl get pods --namespace $(HELM_NAMESPACE)

.PHONY: h-lint
h-package: ## HELM Lint for check the helm files.
	helm lint helm-package

.PHONY: h-package
h-package: k-lint ## HELM Package. Run Helm lint Before. IF is OK, creates a new tar.gz package
	helm package helm-package

.PHONY: h-install
h-install: ## HELM Install the Package Locally.
	helm install $(HELM_RELEASE_NAME) helm-package --namespace $(HELM_NAMESPACE)

.PHONY: h-upgrade
h-upgrade: ## HELM upgrade the current release
	helm upgrade $(HELM_RELEASE_NAME) helm-package --namespace $(HELM_NAMESPACE)

.PHONY: h-list
h-list: ## HELM status show the current release status
	helm list --namespace $(HELM_NAMESPACE)

.PHONY: h-status
h-status: ## HELM status show the current release status
	helm status $(HELM_RELEASE_NAME) --namespace $(HELM_NAMESPACE)

.PHONY: h-delete
h-delete: ## HELM status show the current release status
	helm  delete $(HELM_RELEASE_NAME) --namespace $(HELM_NAMESPACE)