include .env

compose=docker-compose
config= --env-file .env
DOCKER_IMAGE=${DOCKER_REGISTRY}/${DOCKER_REPOSITORY}
PHONY: help
help:
	@awk 'BEGIN {FS = ":.*?## "} /^[a-zA-Z_-]+:.*?## / {printf "\033[36m%-15s\033[0m %s\n", $$1, $$2}' $(MAKEFILE_LIST)

.PHONY: m-package
m-package: ## MAVEN Package. Create the JAR app.
	mvn -Drevision=$(APP_VERSION) clean package

.PHONY: m-verify
m-verify: ## MAVEN Verify Package. Run test
	mvn verify

.PHONY: m-build
m-build: ## MAVEN Build Docker image using maven build plugin
    mvn spring-boot:build-image

.PHONY: m-install
m-install: ## MAVEN Install, create final JAR and copy to local repository
	mvn -Drevision=$(APP_VERSION) clean install

.PHONY: m-run
m-run: ## MAVEN Run the app
    mvn spring-boot:run

.PHONY: d-build
d-build: ## DOCKER Build image using maven build plugin
	docker build --no-cache -t ${DOCKER_IMAGE}:${TAG} .

.PHONY: d-scan
d-scan: ## DOCKER Scan Image
	docker scan  ${DOCKER_IMAGE}:${TAG}

.PHONY: d-run
d-run: ## DOCKER Run default ports
	docker run --rm --name ${CONTAINER_NAME} ${DOCKER_IMAGE}:${TAG} -p ${LOCAL_PORT}:${CONTAINER_PORT}  -p 8443:8443

.PHONY: d-push
d-push: ## DOCKER Push run default ports
	docker push  ${DOCKER_IMAGE}:${TAG}

.PHONY: d-stop
d-stop: ## DOCKER Stop all containers.
	docker stop $(docker ps -q)

.PHONY: d-kill
d-kill: ## DOCKER Kill all containers.
	 docker kill $(docker ps -q)

.PHONY: d-rm
d-rm: ## DOCKER Removes all containers.
	docker rm $(docker ps -a -q)

.PHONY: d-rmi
d-rmi: ## DOCKER Removes all images.
	docker rmi $(docker images -a -q)

.PHONY: d-rmdi
d-rmdi: ## DOCKER Removes dangling images.
	docker rmi $(docker images -a --filter=dangling=true -q)

.PHONY: d-rm-exited-containers
d-rm-exited-containers: ## DOCKER Removes containers that are done.
	docker rm -v $(docker ps -a -q -f status=exited)

.PHONY: d-prune
d-prune: d-kill ## DOCKER Removes containers that are done.
	docker system prune

.PHONY: c-up-from-scratch
c-up-from-scratch: m-install c-restart ##  DOCKER COMPOSE Recreate artifact, docker image and redeploy de Compose

.PHONY: c-up
c-up: ##  DOCKER-COMPOSE Start all or c=<name> containers in foreground
	${compose} up -d --force-recreate --build $(c)

.PHONY: c-start
c-start: ##  DOCKER-COMPOSE Start all or c=<name> containers in background
	${compose} up -d $(c)

.PHONY: c-stop
c-stop: ##  DOCKER-COMPOSE Stop all or c=<name> containers
	${compose} stop $(c)

.PHONY: c-restart
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
h-lint: ## HELM Lint for check the helm files.
	helm lint helm-package

.PHONY: h-package
h-package:  ## HELM Package. Run Helm lint Before. IF is OK, creates a new tar.gz package
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

.PHONY: build
build: m-verify m-install d-build ## App BUILD , means --> (Maven full process) AND (Create the DOCKER Image)

.PHONY: ship
ship: d-push h-lint h-package ## App SHIP , means --> (Maven full process) AND (Create the DOCKER Image)

.PHONY: run
run: h-install ## App BUILD , means --> (Maven full process) AND (Create the DOCKER Image)

-PHONY: local-demo-start
local-demo-start: ##  LOCAL DOCKER-COMPOSE Demo UP with the current image published
	${compose} -f local_demo/docker-compose-local-demo.yml up -d

-PHONY: local-demo-stop
local-demo-stop: ##  LOCAL DOCKER-COMPOSE Demo DOWN with the current image published
	${compose} -f local_demo/docker-compose-local-demo.yml down