#!/bin/sh
docker exec -i psql psql -U root -d ecom_platform < ./seed-template.sql