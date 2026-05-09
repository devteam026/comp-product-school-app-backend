#!/bin/bash

# ============================================
# Multi-Tenant School DB Setup Script
# ============================================

# Usage:
# ./create-tenant.sh <tenant_key> <db_user> <db_password> <mysql_root_user> <mysql_root_password>
#
# Example:
# ./create-tenant.sh ivs_school ivs Ivs@083 root RootPassword
#
# ============================================

TENANT_KEY=$1
DB_USER=$2
DB_PASSWORD=$3
MYSQL_ROOT_USER=$4
MYSQL_ROOT_PASSWORD=$5

DB_HOST="localhost"
MASTER_DB="master"

if [ -z "$TENANT_KEY" ] || [ -z "$DB_USER" ] || [ -z "$DB_PASSWORD" ] || [ -z "$MYSQL_ROOT_USER" ] || [ -z "$MYSQL_ROOT_PASSWORD" ]; then
  echo "Usage:"
  echo "./create-tenant.sh <tenant_key> <db_user> <db_password> <mysql_root_user> <mysql_root_password>"
  exit 1
fi

echo "========================================="
echo "Creating Tenant : $TENANT_KEY"
echo "========================================="

# ------------------------------------------
# Step 1 : Create Database + User
# ------------------------------------------

mysql -h $DB_HOST -u$MYSQL_ROOT_USER -p$MYSQL_ROOT_PASSWORD <<EOF

CREATE DATABASE IF NOT EXISTS \`${TENANT_KEY}\`;

CREATE USER IF NOT EXISTS '${DB_USER}'@'%' IDENTIFIED BY '${DB_PASSWORD}';

-- Full access to tenant DB
GRANT ALL PRIVILEGES ON \`${TENANT_KEY}\`.* TO '${DB_USER}'@'%';

-- Read-only access to master DB
GRANT SELECT ON ${MASTER_DB}.* TO '${DB_USER}'@'%';

FLUSH PRIVILEGES;

EOF

if [ $? -ne 0 ]; then
  echo "❌ Failed to create database/user"
  exit 1
fi

echo "✅ Database and user created"

# ------------------------------------------
# Step 2 : Insert Tenant Entry
# ------------------------------------------

JDBC_URL="jdbc:mysql://${DB_HOST}:3306/${TENANT_KEY}?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true"

mysql -h $DB_HOST -u$MYSQL_ROOT_USER -p$MYSQL_ROOT_PASSWORD <<EOF

INSERT INTO ${MASTER_DB}.tenants
(
    tenant_key,
    db_url,
    db_username,
    db_password
)
VALUES
(
    '${TENANT_KEY}',
    '${JDBC_URL}',
    '${DB_USER}',
    '${DB_PASSWORD}'
);

EOF

if [ $? -ne 0 ]; then
  echo "❌ Failed to insert tenant entry"
  exit 1
fi

echo "✅ Tenant entry added to master.tenants"

echo ""
echo "========================================="
echo "Tenant Setup Completed Successfully"
echo "========================================="
echo "Tenant Key : $TENANT_KEY"
echo "Database   : $TENANT_KEY"
echo "DB User    : $DB_USER"
echo ""
echo "Permissions:"
echo "✔ Full access to ${TENANT_KEY}"
echo "✔ Read-only access to master DB"
echo "========================================="
echo ""
echo "Next Step:"
echo "1. Set tenant '$TENANT_KEY' as default tenant in application config"
echo "2. Restart the Spring Boot application"
echo "3. Hibernate/JPA will auto-create tables"