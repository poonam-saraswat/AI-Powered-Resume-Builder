.PHONY: help up down logs ps build rebuild clean test

help:
	@echo "make up               - start everything"
	@echo "make down             - stop everything"
	@echo "make logs svc=NAME    - tail logs for one service"
	@echo "make ps               - list running containers"
	@echo "make build            - build all images"
	@echo "make rebuild svc=NAME - rebuild and restart one service"
	@echo "make clean            - down + remove volumes (DESTROYS DBs)"
	@echo "make test             - run mvn tests for all modules"

up:
	docker-compose up -d --build

down:
	docker-compose down

logs:
	docker-compose logs -f $(svc)

ps:
	docker-compose ps

build:
	docker-compose build

rebuild:
	docker-compose up -d --build $(svc)

clean:
	docker-compose down -v

test:
	mvn -q test
