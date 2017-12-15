/*
Script generado por Aqua Data Studio 7.5.0 en oct-12-2012 10:39:59 AM
Base de datos: ha
Esquema: <DEFAULT>
Objetos: TABLE
*/
ALTER TABLE "users"
	DROP FOREIGN KEY "FK6A68E0876A673C0";
ALTER TABLE "users_roles"
	DROP FOREIGN KEY "FKF6CCD9C6CB2AF4B4";
ALTER TABLE "users_roles"
	DROP FOREIGN KEY "FKF6CCD9C6260030D4";
ALTER TABLE "roles_permissions"
	DROP FOREIGN KEY "FK250AE025ED97AB4";
ALTER TABLE "roles_permissions"
	DROP FOREIGN KEY "FK250AE02260030D4";
ALTER TABLE "permissions"
	DROP FOREIGN KEY "FK4392F484FEDD6CD4";
ALTER TABLE "permission_representations"
	DROP FOREIGN KEY "FKC5825ED65ED97AB4";
ALTER TABLE "users"
	DROP CONSTRAINT "login";
DROP INDEX "login" ON users;
DROP INDEX "FK6A68E0876A673C0" ON users;
DROP INDEX "FKF6CCD9C6CB2AF4B4" ON users_roles;
DROP INDEX "FKF6CCD9C6260030D4" ON users_roles;
DROP INDEX "FK250AE02260030D4" ON roles_permissions;
DROP INDEX "FK250AE025ED97AB4" ON roles_permissions;
DROP INDEX "FK4392F484FEDD6CD4" ON permissions;
DROP INDEX "FKC5825ED65ED97AB4" ON permission_representations;
DROP TABLE "users";
DROP TABLE "users_roles";
DROP TABLE "roles";
DROP TABLE "roles_permissions";
DROP TABLE "resources";
DROP TABLE "realms";
DROP TABLE "permissions";
DROP TABLE "permission_representations";

CREATE TABLE "permission_representations" ( 
	"id"            	bigint(20) AUTO_INCREMENT NOT NULL,
	"created_at"    	datetime NOT NULL,
	"updated_at"    	datetime NOT NULL,
	"lock_version"  	int(11) NULL,
	"representation"	varchar(255) NULL,
	"permission_id" 	bigint(20) NULL,
	PRIMARY KEY("id")
);
CREATE TABLE "permissions" ( 
	"id"            	bigint(20) AUTO_INCREMENT NOT NULL,
	"created_at"    	datetime NOT NULL,
	"updated_at"    	datetime NOT NULL,
	"lock_version"  	int(11) NULL,
	"app_permission"	tinyint(1) NULL,
	"description"   	varchar(255) NULL,
	"name"          	varchar(25) NOT NULL,
	"resource_id"   	bigint(20) NULL,
	PRIMARY KEY("id")
);
CREATE TABLE "realms" ( 
	"id"                       	bigint(20) AUTO_INCREMENT NOT NULL,
	"created_at"               	datetime NOT NULL,
	"updated_at"               	datetime NOT NULL,
	"lock_version"             	int(11) NULL,
	"changing_password_enabled"	tinyint(1) NULL,
	"description"              	varchar(255) NULL,
	"handled_by"               	varchar(25) NOT NULL,
	"name"                     	varchar(25) NOT NULL,
	PRIMARY KEY("id")
);
CREATE TABLE "resources" ( 
	"id"            	bigint(20) AUTO_INCREMENT NOT NULL,
	"created_at"    	datetime NOT NULL,
	"updated_at"    	datetime NOT NULL,
	"lock_version"  	int(11) NULL,
	"description"   	varchar(255) NULL,
	"name"          	varchar(25) NOT NULL,
	"representation"	varchar(25) NOT NULL,
	PRIMARY KEY("id")
);
CREATE TABLE "roles_permissions" ( 
	"permission_id"	bigint(20) NOT NULL,
	"role_id"      	bigint(20) NOT NULL,
	PRIMARY KEY("permission_id","role_id")
);
CREATE TABLE "roles" ( 
	"id"          	bigint(20) AUTO_INCREMENT NOT NULL,
	"created_at"  	datetime NOT NULL,
	"updated_at"  	datetime NOT NULL,
	"lock_version"	int(11) NULL,
	"description" 	varchar(255) NULL,
	"name"        	varchar(25) NOT NULL,
	PRIMARY KEY("id")
);
CREATE TABLE "users_roles" ( 
	"role_id"	bigint(20) NOT NULL,
	"user_id"	bigint(20) NOT NULL,
	PRIMARY KEY("role_id","user_id")
);
CREATE TABLE "users" ( 
	"id"             	bigint(20) AUTO_INCREMENT NOT NULL,
	"created_at"     	datetime NOT NULL,
	"updated_at"     	datetime NOT NULL,
	"lock_version"   	int(11) NULL,
	"active"         	tinyint(1) NOT NULL,
	"first_name"     	varchar(25) NOT NULL,
	"last_name"      	varchar(25) NOT NULL,
	"login"          	varchar(25) NOT NULL,
	"main_email"     	varchar(35) NOT NULL,
	"middle_name"    	varchar(25) NULL,
	"mobile_number"  	varchar(25) NULL,
	"password"       	varchar(15) NOT NULL,
	"phone_extension"	varchar(6) NULL,
	"phone_number"   	varchar(25) NOT NULL,
	"secondary_email"	varchar(35) NULL,
	"realm_id"       	bigint(20) NULL,
	PRIMARY KEY("id")
);

CREATE INDEX "FKC5825ED65ED97AB4"
	ON "permission_representations"("permission_id");
CREATE INDEX "FK4392F484FEDD6CD4"
	ON "permissions"("resource_id");
CREATE INDEX "FK250AE02260030D4"
	ON "roles_permissions"("role_id");
CREATE INDEX "FK250AE025ED97AB4"
	ON "roles_permissions"("permission_id");
CREATE INDEX "FKF6CCD9C6CB2AF4B4"
	ON "users_roles"("user_id");
CREATE INDEX "FKF6CCD9C6260030D4"
	ON "users_roles"("role_id");
CREATE UNIQUE INDEX "login"
	ON "users"("login", "realm_id");
CREATE INDEX "FK6A68E0876A673C0"
	ON "users"("realm_id");
ALTER TABLE "users"
	ADD CONSTRAINT "login"
	UNIQUE ("login", "realm_id");
ALTER TABLE "permission_representations"
	ADD CONSTRAINT "FKC5825ED65ED97AB4"
	FOREIGN KEY("permission_id")
	REFERENCES "permissions"("id")
	ON DELETE RESTRICT 
	ON UPDATE RESTRICT ;
ALTER TABLE "permissions"
	ADD CONSTRAINT "FK4392F484FEDD6CD4"
	FOREIGN KEY("resource_id")
	REFERENCES "resources"("id")
	ON DELETE RESTRICT 
	ON UPDATE RESTRICT ;
ALTER TABLE "roles_permissions"
	ADD CONSTRAINT "FK250AE025ED97AB4"
	FOREIGN KEY("permission_id")
	REFERENCES "permissions"("id")
	ON DELETE RESTRICT 
	ON UPDATE RESTRICT ;
ALTER TABLE "roles_permissions"
	ADD CONSTRAINT "FK250AE02260030D4"
	FOREIGN KEY("role_id")
	REFERENCES "roles"("id")
	ON DELETE RESTRICT 
	ON UPDATE RESTRICT ;
ALTER TABLE "users_roles"
	ADD CONSTRAINT "FKF6CCD9C6CB2AF4B4"
	FOREIGN KEY("user_id")
	REFERENCES "users"("id")
	ON DELETE RESTRICT 
	ON UPDATE RESTRICT ;
ALTER TABLE "users_roles"
	ADD CONSTRAINT "FKF6CCD9C6260030D4"
	FOREIGN KEY("role_id")
	REFERENCES "roles"("id")
	ON DELETE RESTRICT 
	ON UPDATE RESTRICT ;
ALTER TABLE "users"
	ADD CONSTRAINT "FK6A68E0876A673C0"
	FOREIGN KEY("realm_id")
	REFERENCES "realms"("id")
	ON DELETE RESTRICT 
	ON UPDATE RESTRICT ;
