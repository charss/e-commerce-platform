#!/bin/sh
psql -U "root" -d "ecom-platform" <<EOF
CREATE SCHEMA IF NOT EXISTS auth;
CREATE SCHEMA IF NOT EXISTS product;
CREATE SCHEMA IF NOT EXISTS orders;
CREATE SCHEMA IF NOT EXISTS shopping_cart;
EOF