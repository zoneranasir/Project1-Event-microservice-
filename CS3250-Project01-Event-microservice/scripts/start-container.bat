docker-compose -p "cs-3250" build
docker network create swdev

docker-compose -p "cs-3250" up

docker stop swdev-springboot
docker stop swdev-postgres

docker-compose -p "cs-3250" down
