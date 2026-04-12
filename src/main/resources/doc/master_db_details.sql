CREATE TABLE master.tenants (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  tenant_key VARCHAR(64) NOT NULL UNIQUE,
  db_url VARCHAR(255) NOT NULL,
  db_username VARCHAR(128) NOT NULL,
  db_password VARCHAR(128) NOT NULL,
  active TINYINT(1) NOT NULL DEFAULT 1,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

1. Make an entry in the master database

INSERT INTO master.tenants (tenant_key, db_url, db_username, db_password)
VALUES (
  'viru_school',
  'jdbc:mysql://174.138.105.54:3306/viru_school?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true',
  'viru',
  'Viru@083'
);


INSERT INTO master.tenants (tenant_key, db_url, db_username, db_password)
VALUES (
  'prabhat_school',
  'jdbc:mysql://174.138.105.54:3306/prabhat_school?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true',
  'prabhat',
  'Prabhat@083'
);

1. make new tanent as default for services and then restart the app, it will create all required tables


