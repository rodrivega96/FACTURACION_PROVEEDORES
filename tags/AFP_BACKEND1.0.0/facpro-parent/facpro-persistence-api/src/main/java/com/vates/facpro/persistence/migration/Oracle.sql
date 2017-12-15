//Se limpian tablas. ejecutar consulta y luego ejecutar resultado para eliminar tablas.
select 'drop table '||TABLE_NAME||' cascade constraints
/'  from user_tables
y
select 'drop sequence '||SEQUENCE_NAME||' 
/'  from user_sequences

Para crear dblink
create database link FACTURACION
  connect to facproadm identified by facproadm
  using
  '(DESCRIPTION=
    (ADDRESS=
     (PROTOCOL=TCP)
     (HOST=vates5)
     (PORT=1521))
    (CONNECT_DATA=
     (SID=DESA10G)))'
  /

/
//INICIO PERMISOS
INSERT INTO RESOURCES (ID, CREATED_AT, UPDATED_AT, LOCK_VERSION, DESCRIPTION, NAME, REPRESENTATION, ORD)
VALUES (1, SYSDATE, SYSDATE, 0, null,'USERS','Usuarios', 2)
/
INSERT INTO PERMISSIONS (ID, CREATED_AT, UPDATED_AT, LOCK_VERSION, DESCRIPTION, NAME, RESOURCE_ID)
VALUES (1, SYSDATE, SYSDATE, 0, null,'ADMINISTRAR',1)
/
INSERT INTO PERMISSION_REPRESENTATIONS (ID, CREATED_AT, UPDATED_AT, LOCK_VERSION, REPRESENTATION, PERMISSION_ID, REPRESENTATIONPAGE, ORD)
VALUES (1, SYSDATE, SYSDATE, 0,'Administración de Usuarios',1,'user-admin',1)
/
INSERT INTO RESOURCES (ID, CREATED_AT, UPDATED_AT, LOCK_VERSION, DESCRIPTION, NAME, REPRESENTATION, ORD)
VALUES (2, SYSDATE, SYSDATE, 0, null,'user::userPaginatedList','user::userPaginatedList',0)
/
INSERT INTO PERMISSIONS (ID, CREATED_AT, UPDATED_AT, LOCK_VERSION, DESCRIPTION, NAME, RESOURCE_ID)
VALUES (2, SYSDATE, SYSDATE, 0, null,'EJECUTAR',2)
/
INSERT INTO PERMISSION_REPRESENTATIONS (ID, CREATED_AT, UPDATED_AT, LOCK_VERSION, REPRESENTATION, PERMISSION_ID, REPRESENTATIONPAGE, ORD)
VALUES (2, SYSDATE, SYSDATE, 0,'GET',2,null,0)
/
INSERT INTO RESOURCES (ID, CREATED_AT, UPDATED_AT, LOCK_VERSION, DESCRIPTION, NAME, REPRESENTATION, ORD)
VALUES (3, SYSDATE, SYSDATE, 0, null,'user::getUser','user::getUser',0)
/
INSERT INTO PERMISSIONS (ID, CREATED_AT, UPDATED_AT, LOCK_VERSION, DESCRIPTION, NAME, RESOURCE_ID)
VALUES (3, SYSDATE, SYSDATE, 0, null,'EJECUTAR',3)
/
INSERT INTO PERMISSION_REPRESENTATIONS (ID, CREATED_AT, UPDATED_AT, LOCK_VERSION, REPRESENTATION, PERMISSION_ID, REPRESENTATIONPAGE, ORD)
VALUES (3, SYSDATE, SYSDATE, 0,'GET',3,null,0)
/
INSERT INTO RESOURCES (ID, CREATED_AT, UPDATED_AT, LOCK_VERSION, DESCRIPTION, NAME, REPRESENTATION, ORD)
VALUES (4, SYSDATE, SYSDATE, 0, null,'user::saveUser','user::saveUser',0)
/
INSERT INTO PERMISSIONS (ID, CREATED_AT, UPDATED_AT, LOCK_VERSION, DESCRIPTION, NAME, RESOURCE_ID)
VALUES (4, SYSDATE, SYSDATE, 0, null,'EJECUTAR',4)
/
INSERT INTO PERMISSION_REPRESENTATIONS (ID, CREATED_AT, UPDATED_AT, LOCK_VERSION, REPRESENTATION, PERMISSION_ID, REPRESENTATIONPAGE, ORD)
VALUES (4, SYSDATE, SYSDATE, 0,'POST',4,null,0)
/
INSERT INTO RESOURCES (ID, CREATED_AT, UPDATED_AT, LOCK_VERSION, DESCRIPTION, NAME, REPRESENTATION, ORD)
VALUES (5, SYSDATE, SYSDATE, 0, null,'user::deleteUser','user::deleteUser',0)
/
INSERT INTO PERMISSIONS (ID, CREATED_AT, UPDATED_AT, LOCK_VERSION, DESCRIPTION, NAME, RESOURCE_ID)
VALUES (5, SYSDATE, SYSDATE, 0, null,'EJECUTAR',5)
/
INSERT INTO PERMISSION_REPRESENTATIONS (ID, CREATED_AT, UPDATED_AT, LOCK_VERSION, REPRESENTATION, PERMISSION_ID, REPRESENTATIONPAGE, ORD)
VALUES (5, SYSDATE, SYSDATE, 0,'POST',5,null,0)
/
INSERT INTO RESOURCES (ID, CREATED_AT, UPDATED_AT, LOCK_VERSION, DESCRIPTION, NAME, REPRESENTATION, ORD)
VALUES (6, SYSDATE, SYSDATE, 0, null,'user::forceDeleteUser','user::forceDeleteUser',0)
/
INSERT INTO PERMISSIONS (ID, CREATED_AT, UPDATED_AT, LOCK_VERSION, DESCRIPTION, NAME, RESOURCE_ID)
VALUES (6, SYSDATE, SYSDATE, 0, null,'EJECUTAR',6)
/
INSERT INTO PERMISSION_REPRESENTATIONS (ID, CREATED_AT, UPDATED_AT, LOCK_VERSION, REPRESENTATION, PERMISSION_ID, REPRESENTATIONPAGE, ORD)
VALUES (6, SYSDATE, SYSDATE, 0,'POST',6,null,0)
/
INSERT INTO RESOURCES (ID, CREATED_AT, UPDATED_AT, LOCK_VERSION, DESCRIPTION, NAME, REPRESENTATION, ORD)
VALUES (7, SYSDATE, SYSDATE, 0, null,'user::updateUser','user::updateUser',0)
/
INSERT INTO PERMISSIONS (ID, CREATED_AT, UPDATED_AT, LOCK_VERSION, DESCRIPTION, NAME, RESOURCE_ID)
VALUES (7, SYSDATE, SYSDATE, 0, null,'EJECUTAR',7)
/
INSERT INTO PERMISSION_REPRESENTATIONS (ID, CREATED_AT, UPDATED_AT, LOCK_VERSION, REPRESENTATION, PERMISSION_ID, REPRESENTATIONPAGE, ORD)
VALUES (7, SYSDATE, SYSDATE, 0,'POST',7,null,0)
/
INSERT INTO RESOURCES (ID, CREATED_AT, UPDATED_AT, LOCK_VERSION, DESCRIPTION, NAME, REPRESENTATION, ORD)
VALUES (8, SYSDATE, SYSDATE, 0, null,'user::forceUpdateUser','user::forceUpdateUser',0)
/
INSERT INTO PERMISSIONS (ID, CREATED_AT, UPDATED_AT, LOCK_VERSION, DESCRIPTION, NAME, RESOURCE_ID)
VALUES (8, SYSDATE, SYSDATE, 0, null,'EJECUTAR',8)
/
INSERT INTO PERMISSION_REPRESENTATIONS (ID, CREATED_AT, UPDATED_AT, LOCK_VERSION, REPRESENTATION, PERMISSION_ID, REPRESENTATIONPAGE, ORD)
VALUES (8, SYSDATE, SYSDATE, 0,'POST',8,null,0)
/
INSERT INTO RESOURCES (ID, CREATED_AT, UPDATED_AT, LOCK_VERSION, DESCRIPTION, NAME, REPRESENTATION, ORD)
VALUES (9, SYSDATE, SYSDATE, 0, null,'FACTURAS','Facturas', 1)
/
INSERT INTO PERMISSIONS (ID, CREATED_AT, UPDATED_AT, LOCK_VERSION, DESCRIPTION, NAME, RESOURCE_ID)
VALUES (9, SYSDATE, SYSDATE, 0, null,'ADMINISTRAR',9)
/
INSERT INTO PERMISSION_REPRESENTATIONS (ID, CREATED_AT, UPDATED_AT, LOCK_VERSION, REPRESENTATION, PERMISSION_ID, REPRESENTATIONPAGE, ORD)
VALUES (9, SYSDATE, SYSDATE, 0,'Gestion de Facturas',9,'factura-admin',1)
/
INSERT INTO RESOURCES (ID, CREATED_AT, UPDATED_AT, LOCK_VERSION, DESCRIPTION, NAME, REPRESENTATION, ORD)
VALUES (10, SYSDATE, SYSDATE, 0, null,'factura::facturaPaginatedList','factura::facturaPaginatedList',0)
/
INSERT INTO PERMISSIONS (ID, CREATED_AT, UPDATED_AT, LOCK_VERSION, DESCRIPTION, NAME, RESOURCE_ID)
VALUES (10, SYSDATE, SYSDATE, 0, null,'EJECUTAR',10)
/
INSERT INTO PERMISSION_REPRESENTATIONS (ID, CREATED_AT, UPDATED_AT, LOCK_VERSION, REPRESENTATION, PERMISSION_ID, REPRESENTATIONPAGE, ORD)
VALUES (10, SYSDATE, SYSDATE, 0,'GET',10,null,0)
/
INSERT INTO RESOURCES (ID, CREATED_AT, UPDATED_AT, LOCK_VERSION, DESCRIPTION, NAME, REPRESENTATION, ORD)
VALUES (11, SYSDATE, SYSDATE, 0, null,'factura::saveFactura','factura::saveFactura',0)
/
INSERT INTO PERMISSIONS (ID, CREATED_AT, UPDATED_AT, LOCK_VERSION, DESCRIPTION, NAME, RESOURCE_ID)
VALUES (11, SYSDATE, SYSDATE, 0, null,'EJECUTAR',11)
/
INSERT INTO PERMISSION_REPRESENTATIONS (ID, CREATED_AT, UPDATED_AT, LOCK_VERSION, REPRESENTATION, PERMISSION_ID, REPRESENTATIONPAGE, ORD)
VALUES (11, SYSDATE, SYSDATE, 0,'POST',11,null,0)
/
INSERT INTO RESOURCES (ID, CREATED_AT, UPDATED_AT, LOCK_VERSION, DESCRIPTION, NAME, REPRESENTATION, ORD)
VALUES (12, SYSDATE, SYSDATE, 0, null,'factura::getFactura','factura::getFactura',0)
/
INSERT INTO PERMISSIONS (ID, CREATED_AT, UPDATED_AT, LOCK_VERSION, DESCRIPTION, NAME, RESOURCE_ID)
VALUES (12, SYSDATE, SYSDATE, 0, null,'EJECUTAR',12)
/
INSERT INTO PERMISSION_REPRESENTATIONS (ID, CREATED_AT, UPDATED_AT, LOCK_VERSION, REPRESENTATION, PERMISSION_ID, REPRESENTATIONPAGE, ORD)
VALUES (12, SYSDATE, SYSDATE, 0,'GET',12,null,0)
/
INSERT INTO RESOURCES (ID, CREATED_AT, UPDATED_AT, LOCK_VERSION, DESCRIPTION, NAME, REPRESENTATION, ORD)
VALUES (13, SYSDATE, SYSDATE, 0, null,'factura::updateFactura','factura::updateFactura',0)
/
INSERT INTO PERMISSIONS (ID, CREATED_AT, UPDATED_AT, LOCK_VERSION, DESCRIPTION, NAME, RESOURCE_ID)
VALUES (13, SYSDATE, SYSDATE, 0, null,'EJECUTAR',13)
/
INSERT INTO PERMISSION_REPRESENTATIONS (ID, CREATED_AT, UPDATED_AT, LOCK_VERSION, REPRESENTATION, PERMISSION_ID, REPRESENTATIONPAGE, ORD)
VALUES (13, SYSDATE, SYSDATE, 0,'POST',13,null,0)
/
INSERT INTO RESOURCES (ID, CREATED_AT, UPDATED_AT, LOCK_VERSION, DESCRIPTION, NAME, REPRESENTATION, ORD)
VALUES (14, SYSDATE, SYSDATE, 0, null,'role::getRolesByUserId','role::getRolesByUserId',0)
/
INSERT INTO PERMISSIONS (ID, CREATED_AT, UPDATED_AT, LOCK_VERSION, DESCRIPTION, NAME, RESOURCE_ID)
VALUES (14, SYSDATE, SYSDATE, 0, null,'EJECUTAR',14)
/
INSERT INTO PERMISSION_REPRESENTATIONS (ID, CREATED_AT, UPDATED_AT, LOCK_VERSION, REPRESENTATION, PERMISSION_ID, REPRESENTATIONPAGE, ORD)
VALUES (14, SYSDATE, SYSDATE, 0,'GET',14,null,0)
/
INSERT INTO RESOURCES (ID, CREATED_AT, UPDATED_AT, LOCK_VERSION, DESCRIPTION, NAME, REPRESENTATION, ORD)
VALUES (15, SYSDATE, SYSDATE, 0, null,'role::saveRoles','role::saveRoles',0)
/
INSERT INTO PERMISSIONS (ID, CREATED_AT, UPDATED_AT, LOCK_VERSION, DESCRIPTION, NAME, RESOURCE_ID)
VALUES (15, SYSDATE, SYSDATE, 0, null,'EJECUTAR',15)
/
INSERT INTO PERMISSION_REPRESENTATIONS (ID, CREATED_AT, UPDATED_AT, LOCK_VERSION, REPRESENTATION, PERMISSION_ID, REPRESENTATIONPAGE, ORD)
VALUES (15, SYSDATE, SYSDATE, 0,'POST',15,null,0)
/
INSERT INTO RESOURCES (ID, CREATED_AT, UPDATED_AT, LOCK_VERSION, DESCRIPTION, NAME, REPRESENTATION, ORD)
VALUES (16, SYSDATE, SYSDATE, 0, null,'archivoFactura::uploadFile','archivoFactura::uploadFile',0)
/
INSERT INTO PERMISSIONS (ID, CREATED_AT, UPDATED_AT, LOCK_VERSION, DESCRIPTION, NAME, RESOURCE_ID)
VALUES (16, SYSDATE, SYSDATE, 0, null,'EJECUTAR',16)
/
INSERT INTO PERMISSION_REPRESENTATIONS (ID, CREATED_AT, UPDATED_AT, LOCK_VERSION, REPRESENTATION, PERMISSION_ID, REPRESENTATIONPAGE, ORD)
VALUES (16, SYSDATE, SYSDATE, 0,'POST',16,null,0)
/
INSERT INTO RESOURCES (ID, CREATED_AT, UPDATED_AT, LOCK_VERSION, DESCRIPTION, NAME, REPRESENTATION, ORD)
VALUES (17, SYSDATE, SYSDATE, 0, null,'archivoFactura::getAllArchivoFactura','archivoFactura::getAllArchivoFactura',0)
/
INSERT INTO PERMISSIONS (ID, CREATED_AT, UPDATED_AT, LOCK_VERSION, DESCRIPTION, NAME, RESOURCE_ID)
VALUES (17, SYSDATE, SYSDATE, 0, null,'EJECUTAR',17)
/
INSERT INTO PERMISSION_REPRESENTATIONS (ID, CREATED_AT, UPDATED_AT, LOCK_VERSION, REPRESENTATION, PERMISSION_ID, REPRESENTATIONPAGE, ORD)
VALUES (17, SYSDATE, SYSDATE, 0,'GET',17,null,0)
/
INSERT INTO RESOURCES (ID, CREATED_AT, UPDATED_AT, LOCK_VERSION, DESCRIPTION, NAME, REPRESENTATION, ORD)
VALUES (18, SYSDATE, SYSDATE, 0, null,'archivo::downloadArchivo','archivo::downloadArchivo',0)
/
INSERT INTO PERMISSIONS (ID, CREATED_AT, UPDATED_AT, LOCK_VERSION, DESCRIPTION, NAME, RESOURCE_ID)
VALUES (18, SYSDATE, SYSDATE, 0, null,'EJECUTAR',18)
/
INSERT INTO PERMISSION_REPRESENTATIONS (ID, CREATED_AT, UPDATED_AT, LOCK_VERSION, REPRESENTATION, PERMISSION_ID, REPRESENTATIONPAGE, ORD)
VALUES (18, SYSDATE, SYSDATE, 0,'GET',18,null,0)
/
INSERT INTO RESOURCES (ID, CREATED_AT, UPDATED_AT, LOCK_VERSION, DESCRIPTION, NAME, REPRESENTATION, ORD)
VALUES (19, SYSDATE, SYSDATE, 0, null,'archivo::deleteArchivo','archivo::deleteArchivo',0)
/
INSERT INTO PERMISSIONS (ID, CREATED_AT, UPDATED_AT, LOCK_VERSION, DESCRIPTION, NAME, RESOURCE_ID)
VALUES (19, SYSDATE, SYSDATE, 0, null,'EJECUTAR',19)
/
INSERT INTO PERMISSION_REPRESENTATIONS (ID, CREATED_AT, UPDATED_AT, LOCK_VERSION, REPRESENTATION, PERMISSION_ID, REPRESENTATIONPAGE, ORD)
VALUES (19, SYSDATE, SYSDATE, 0,'POST',19,null,0)
/
INSERT INTO RESOURCES (ID, CREATED_AT, UPDATED_AT, LOCK_VERSION, DESCRIPTION, NAME, REPRESENTATION, ORD)
VALUES (20, SYSDATE, SYSDATE, 0, null,'AUTORIZACION','Autorización', 3)
/
INSERT INTO PERMISSIONS (ID, CREATED_AT, UPDATED_AT, LOCK_VERSION, DESCRIPTION, NAME, RESOURCE_ID)
VALUES (20, SYSDATE, SYSDATE, 0, null,'ADMINISTRAR',20)
/
INSERT INTO PERMISSION_REPRESENTATIONS (ID, CREATED_AT, UPDATED_AT, LOCK_VERSION, REPRESENTATION, PERMISSION_ID, REPRESENTATIONPAGE, ORD)
VALUES (20, SYSDATE, SYSDATE, 0,'Autorizaciones Pendientes',20,'autorizacion-admin',1)
/
INSERT INTO RESOURCES (ID, CREATED_AT, UPDATED_AT, LOCK_VERSION, DESCRIPTION, NAME, REPRESENTATION, ORD)
VALUES (21, SYSDATE, SYSDATE, 0, null,'factura::facturaViewPaginatedList','factura::facturaViewPaginatedList',0)
/
INSERT INTO PERMISSIONS (ID, CREATED_AT, UPDATED_AT, LOCK_VERSION, DESCRIPTION, NAME, RESOURCE_ID)
VALUES (21, SYSDATE, SYSDATE, 0, null,'EJECUTAR',21)
/
INSERT INTO PERMISSION_REPRESENTATIONS (ID, CREATED_AT, UPDATED_AT, LOCK_VERSION, REPRESENTATION, PERMISSION_ID, REPRESENTATIONPAGE, ORD)
VALUES (21, SYSDATE, SYSDATE, 0,'GET',21,null,0)
/
INSERT INTO RESOURCES (ID, CREATED_AT, UPDATED_AT, LOCK_VERSION, DESCRIPTION, NAME, REPRESENTATION, ORD)
VALUES (22, SYSDATE, SYSDATE, 0, null,'historial::getHistorialByFinalUser','historial::getHistorialByFinalUser',0)
/
INSERT INTO PERMISSIONS (ID, CREATED_AT, UPDATED_AT, LOCK_VERSION, DESCRIPTION, NAME, RESOURCE_ID)
VALUES (22, SYSDATE, SYSDATE, 0, null,'EJECUTAR',22)
/
INSERT INTO PERMISSION_REPRESENTATIONS (ID, CREATED_AT, UPDATED_AT, LOCK_VERSION, REPRESENTATION, PERMISSION_ID, REPRESENTATIONPAGE, ORD)
VALUES (22, SYSDATE, SYSDATE, 0,'GET',22,null,0)
/
INSERT INTO RESOURCES (ID, CREATED_AT, UPDATED_AT, LOCK_VERSION, DESCRIPTION, NAME, REPRESENTATION, ORD)
VALUES (23, SYSDATE, SYSDATE, 0, null,'user::getAuthorized','user::getAuthorized',0)
/
INSERT INTO PERMISSIONS (ID, CREATED_AT, UPDATED_AT, LOCK_VERSION, DESCRIPTION, NAME, RESOURCE_ID)
VALUES (23, SYSDATE, SYSDATE, 0, null,'EJECUTAR',23)
/
INSERT INTO PERMISSION_REPRESENTATIONS (ID, CREATED_AT, UPDATED_AT, LOCK_VERSION, REPRESENTATION, PERMISSION_ID, REPRESENTATIONPAGE, ORD)
VALUES (23, SYSDATE, SYSDATE, 0,'GET',23,null,0)
/
INSERT INTO RESOURCES (ID, CREATED_AT, UPDATED_AT, LOCK_VERSION, DESCRIPTION, NAME, REPRESENTATION, ORD)
VALUES (24, SYSDATE, SYSDATE, 0, null,'workFlow::getNivelByFactura','workFlow::getNivelByFactura',0)
/
INSERT INTO PERMISSIONS (ID, CREATED_AT, UPDATED_AT, LOCK_VERSION, DESCRIPTION, NAME, RESOURCE_ID)
VALUES (24, SYSDATE, SYSDATE, 0, null,'EJECUTAR',24)
/
INSERT INTO PERMISSION_REPRESENTATIONS (ID, CREATED_AT, UPDATED_AT, LOCK_VERSION, REPRESENTATION, PERMISSION_ID, REPRESENTATIONPAGE, ORD)
VALUES (24, SYSDATE, SYSDATE, 0,'GET',24,null,0)
/
INSERT INTO RESOURCES (ID, CREATED_AT, UPDATED_AT, LOCK_VERSION, DESCRIPTION, NAME, REPRESENTATION, ORD)
VALUES (25, SYSDATE, SYSDATE, 0, null,'workFlow::saveNivel','workFlow::saveNivel',0)
/
INSERT INTO PERMISSIONS (ID, CREATED_AT, UPDATED_AT, LOCK_VERSION, DESCRIPTION, NAME, RESOURCE_ID)
VALUES (25, SYSDATE, SYSDATE, 0, null,'EJECUTAR',25)
/
INSERT INTO PERMISSION_REPRESENTATIONS (ID, CREATED_AT, UPDATED_AT, LOCK_VERSION, REPRESENTATION, PERMISSION_ID, REPRESENTATIONPAGE, ORD)
VALUES (25, SYSDATE, SYSDATE, 0,'POST',25,null,0)
/
INSERT INTO RESOURCES (ID, CREATED_AT, UPDATED_AT, LOCK_VERSION, DESCRIPTION, NAME, REPRESENTATION, ORD)
VALUES (26, SYSDATE, SYSDATE, 0, null,'workFlow::saveAndPublishNivel','workFlow::saveAndPublishNivel',0)
/
INSERT INTO PERMISSIONS (ID, CREATED_AT, UPDATED_AT, LOCK_VERSION, DESCRIPTION, NAME, RESOURCE_ID)
VALUES (26, SYSDATE, SYSDATE, 0, null,'EJECUTAR',26)
/
INSERT INTO PERMISSION_REPRESENTATIONS (ID, CREATED_AT, UPDATED_AT, LOCK_VERSION, REPRESENTATION, PERMISSION_ID, REPRESENTATIONPAGE, ORD)
VALUES (26, SYSDATE, SYSDATE, 0,'POST',26,null,0)
/
--INSERT INTO RESOURCES (ID, CREATED_AT, UPDATED_AT, LOCK_VERSION, DESCRIPTION, NAME, REPRESENTATION, ORD)
--VALUES (27, SYSDATE, SYSDATE, 0, null,'facturaConsult::getFactura','facturaConsult::getFactura', 0)
--/
INSERT INTO PERMISSIONS (ID, CREATED_AT, UPDATED_AT, LOCK_VERSION, DESCRIPTION, NAME, RESOURCE_ID)
VALUES (27, SYSDATE, SYSDATE, 0, null,'EJECUTAR',9)
/
INSERT INTO PERMISSION_REPRESENTATIONS (ID, CREATED_AT, UPDATED_AT, LOCK_VERSION, REPRESENTATION, PERMISSION_ID, REPRESENTATIONPAGE, ORD)
VALUES (27, SYSDATE, SYSDATE, 0,'Consultar Facturas',27,'factura-consult-admin',2)
/
--INSERT INTO RESOURCES (ID, CREATED_AT, UPDATED_AT, LOCK_VERSION, DESCRIPTION, NAME, REPRESENTATION, ORD)
--VALUES (28, SYSDATE, SYSDATE, 0, null,'autorizacionNivelInferior::getAutorizacion','autorizacionNivelInferior::getAutorizacion', 0)
--/
INSERT INTO PERMISSIONS (ID, CREATED_AT, UPDATED_AT, LOCK_VERSION, DESCRIPTION, NAME, RESOURCE_ID)
VALUES (28, SYSDATE, SYSDATE, 0, null,'EJECUTAR',20)
/
INSERT INTO PERMISSION_REPRESENTATIONS (ID, CREATED_AT, UPDATED_AT, LOCK_VERSION, REPRESENTATION, PERMISSION_ID, REPRESENTATIONPAGE, ORD)
VALUES (28, SYSDATE, SYSDATE, 0,'Autorizaciones Pendientes Niveles Inferiores',28,'autorizacion-nivel-inferior',2)
/

INSERT INTO RESOURCES (ID, CREATED_AT, UPDATED_AT, LOCK_VERSION, DESCRIPTION, NAME, REPRESENTATION, ORD)
VALUES (29, SYSDATE, SYSDATE, 0, null,'factura::nivelInferiorList','factura::nivelInferiorList',0)
/
INSERT INTO PERMISSIONS (ID, CREATED_AT, UPDATED_AT, LOCK_VERSION, DESCRIPTION, NAME, RESOURCE_ID)
VALUES (29, SYSDATE, SYSDATE, 0, null,'EJECUTAR',29)
/
INSERT INTO PERMISSION_REPRESENTATIONS (ID, CREATED_AT, UPDATED_AT, LOCK_VERSION, REPRESENTATION, PERMISSION_ID, REPRESENTATIONPAGE, ORD)
VALUES (29, SYSDATE, SYSDATE, 0,'GET',29,null,0)
/
INSERT INTO RESOURCES (ID, CREATED_AT, UPDATED_AT, LOCK_VERSION, DESCRIPTION, NAME, REPRESENTATION, ORD)
VALUES (30, SYSDATE, SYSDATE, 0, null,'historial::saveHistorial','historial::saveHistorial',0)
/
INSERT INTO PERMISSIONS (ID, CREATED_AT, UPDATED_AT, LOCK_VERSION, DESCRIPTION, NAME, RESOURCE_ID)
VALUES (30, SYSDATE, SYSDATE, 0, null,'EJECUTAR',30)
/
INSERT INTO PERMISSION_REPRESENTATIONS (ID, CREATED_AT, UPDATED_AT, LOCK_VERSION, REPRESENTATION, PERMISSION_ID, REPRESENTATIONPAGE, ORD)
VALUES (30, SYSDATE, SYSDATE, 0,'POST',30,null,0)
/
INSERT INTO RESOURCES (ID, CREATED_AT, UPDATED_AT, LOCK_VERSION, DESCRIPTION, NAME, REPRESENTATION, ORD)
VALUES (31, SYSDATE, SYSDATE, 0, null,'historial::rechazarFacturaHistorial','historial::rechazarFacturaHistorial',0)
/
INSERT INTO PERMISSIONS (ID, CREATED_AT, UPDATED_AT, LOCK_VERSION, DESCRIPTION, NAME, RESOURCE_ID)
VALUES (31, SYSDATE, SYSDATE, 0, null,'EJECUTAR',31)
/
INSERT INTO PERMISSION_REPRESENTATIONS (ID, CREATED_AT, UPDATED_AT, LOCK_VERSION, REPRESENTATION, PERMISSION_ID, REPRESENTATIONPAGE, ORD)
VALUES (31, SYSDATE, SYSDATE, 0,'POST',31,null,0)
/
INSERT INTO RESOURCES (ID, CREATED_AT, UPDATED_AT, LOCK_VERSION, DESCRIPTION, NAME, REPRESENTATION, ORD)
VALUES (32, SYSDATE, SYSDATE, 0, null,'historial::saveAnterioresHistorial','historial::saveAnterioresHistorial',0)
/
INSERT INTO PERMISSIONS (ID, CREATED_AT, UPDATED_AT, LOCK_VERSION, DESCRIPTION, NAME, RESOURCE_ID)
VALUES (32, SYSDATE, SYSDATE, 0, null,'EJECUTAR',32)
/
INSERT INTO PERMISSION_REPRESENTATIONS (ID, CREATED_AT, UPDATED_AT, LOCK_VERSION, REPRESENTATION, PERMISSION_ID, REPRESENTATIONPAGE, ORD)
VALUES (32, SYSDATE, SYSDATE, 0,'POST',32,null,0)
/
INSERT INTO RESOURCES (ID, CREATED_AT, UPDATED_AT, LOCK_VERSION, DESCRIPTION, NAME, REPRESENTATION, ORD)
VALUES (33, SYSDATE, SYSDATE, 0, null,'historial::rechazarAnterioresHistorial','historial::rechazarAnterioresHistorial',0)
/
INSERT INTO PERMISSIONS (ID, CREATED_AT, UPDATED_AT, LOCK_VERSION, DESCRIPTION, NAME, RESOURCE_ID)
VALUES (33, SYSDATE, SYSDATE, 0, null,'EJECUTAR',33)
/
INSERT INTO PERMISSION_REPRESENTATIONS (ID, CREATED_AT, UPDATED_AT, LOCK_VERSION, REPRESENTATION, PERMISSION_ID, REPRESENTATIONPAGE, ORD)
VALUES (33, SYSDATE, SYSDATE, 0,'POST',33,null,0)
/
INSERT INTO RESOURCES (ID, CREATED_AT, UPDATED_AT, LOCK_VERSION, DESCRIPTION, NAME, REPRESENTATION, ORD)
VALUES (34, SYSDATE, SYSDATE, 0, null,'login::login','login::login',0)
/
INSERT INTO PERMISSIONS (ID, CREATED_AT, UPDATED_AT, LOCK_VERSION, DESCRIPTION, NAME, RESOURCE_ID)
VALUES (34, SYSDATE, SYSDATE, 0, null,'EJECUTAR',34)
/
INSERT INTO PERMISSION_REPRESENTATIONS (ID, CREATED_AT, UPDATED_AT, LOCK_VERSION, REPRESENTATION, PERMISSION_ID, REPRESENTATIONPAGE, ORD)
VALUES (34, SYSDATE, SYSDATE, 0,'POST',34,null,0)
/
INSERT INTO RESOURCES (ID, CREATED_AT, UPDATED_AT, LOCK_VERSION, DESCRIPTION, NAME, REPRESENTATION, ORD)
VALUES (35, SYSDATE, SYSDATE, 0, null,'login::getMenu','login::getMenu',0)
/
INSERT INTO PERMISSIONS (ID, CREATED_AT, UPDATED_AT, LOCK_VERSION, DESCRIPTION, NAME, RESOURCE_ID)
VALUES (35, SYSDATE, SYSDATE, 0, null,'EJECUTAR',35)
/
INSERT INTO PERMISSION_REPRESENTATIONS (ID, CREATED_AT, UPDATED_AT, LOCK_VERSION, REPRESENTATION, PERMISSION_ID, REPRESENTATIONPAGE, ORD)
VALUES (35, SYSDATE, SYSDATE, 0,'POST',35,null,0)
/
INSERT INTO RESOURCES (ID, CREATED_AT, UPDATED_AT, LOCK_VERSION, DESCRIPTION, NAME, REPRESENTATION, ORD)
VALUES (36, SYSDATE, SYSDATE, 0, null,'facturaConsult::facturaPaginatedList','facturaConsult::facturaPaginatedList',0)
/
INSERT INTO PERMISSIONS (ID, CREATED_AT, UPDATED_AT, LOCK_VERSION, DESCRIPTION, NAME, RESOURCE_ID)
VALUES (36, SYSDATE, SYSDATE, 0, null,'EJECUTAR',36)
/
INSERT INTO PERMISSION_REPRESENTATIONS (ID, CREATED_AT, UPDATED_AT, LOCK_VERSION, REPRESENTATION, PERMISSION_ID, REPRESENTATIONPAGE, ORD)
VALUES (36, SYSDATE, SYSDATE, 0,'GET',36,null,0)
/
INSERT INTO RESOURCES (ID, CREATED_AT, UPDATED_AT, LOCK_VERSION, DESCRIPTION, NAME, REPRESENTATION, ORD)
VALUES (37, SYSDATE, SYSDATE, 0, null,'autorizacionNivelInferior::getAutorizacion','autorizacionNivelInferior::getAutorizacion', 0)
/
INSERT INTO PERMISSIONS (ID, CREATED_AT, UPDATED_AT, LOCK_VERSION, DESCRIPTION, NAME, RESOURCE_ID)
VALUES (37, SYSDATE, SYSDATE, 0, null,'EJECUTAR',37)
/
INSERT INTO PERMISSION_REPRESENTATIONS (ID, CREATED_AT, UPDATED_AT, LOCK_VERSION, REPRESENTATION, PERMISSION_ID, REPRESENTATIONPAGE, ORD)
VALUES (37, SYSDATE, SYSDATE, 0,'GET',37,null,0)
/
//FIN PERMISOS

//INCIO ROLES
INSERT INTO ROLES ( ID,CREATED_AT,UPDATED_AT,LOCK_VERSION,DESCRIPTION,NAME)
VALUES (1, SYSDATE, SYSDATE,0,'','Root')
/
INSERT INTO ROLES ( ID,CREATED_AT,UPDATED_AT,LOCK_VERSION,DESCRIPTION,NAME)
VALUES (2, SYSDATE, SYSDATE,0,'','Administrador')
/
INSERT INTO ROLES ( ID,CREATED_AT,UPDATED_AT,LOCK_VERSION,DESCRIPTION,NAME)
VALUES (3, SYSDATE, SYSDATE,0,'Un usuario con permisos de administrador del sistema podrá acceder a todas las pantallas, cargar WorkFlows (WF) y modificarlos.','Administrador del sistema')
/
INSERT INTO ROLES ( ID,CREATED_AT,UPDATED_AT,LOCK_VERSION,DESCRIPTION,NAME)
VALUES (4, SYSDATE, SYSDATE,0,'Un usuario con permisos de usuario final pueden consultar la pantalla de facturas pendientes y las facturas de niveles inferiores.','Usuario final')
/
INSERT INTO ROLES ( ID,CREATED_AT,UPDATED_AT,LOCK_VERSION,DESCRIPTION,NAME)
VALUES (5, SYSDATE, SYSDATE,0,'Un usuario con permisos de responsable de pagos recibira notificaciones vía mail cuando una factura es observada/rechazada','Responsable de Pagos')
/
INSERT INTO ROLES ( ID,CREATED_AT,UPDATED_AT,LOCK_VERSION,DESCRIPTION,NAME)
VALUES (6, SYSDATE, SYSDATE,0,'Un usuario con permisos de consultar facturas puede consultar todas las facturas.','Consultar Facturas')
/

//FIN ROLES

//INICIO USERS
INSERT INTO USERS (ID, CREATED_AT, UPDATED_AT, LOCK_VERSION, ACTIVE, JOB, LAST_NAME, MAIL, NAME, PASSWORD, TIPO)
VALUES(1, SYSDATE, SYSDATE, 0, 1, '-', '-', 'root@vates.com', '-', '123456', 0)
/
INSERT INTO USERS (ID, CREATED_AT, UPDATED_AT, LOCK_VERSION, ACTIVE, JOB, LAST_NAME, MAIL, NAME, PASSWORD, TIPO)
VALUES(2, SYSDATE, SYSDATE, 0, 1, '-', '-', 'admin@vates.com', '-', '123456', 0)
/
select USER_SEQ.nextval from dual
/
//FIN USERS

//INICIO ROLES PERMISSIONS
INSERT INTO ROLES_PERMISSIONS (PERMISSION_ID,ROLE_ID )
SELECT P.ID,1 FROM PERMISSIONS P
WHERE P.ID IN (1,2,3,4,5,6,7,8,14,15) AND P.ID NOT IN (SELECT PERMISSION_ID FROM ROLES_PERMISSIONS WHERE ROLE_ID=1)
/
INSERT INTO ROLES_PERMISSIONS (PERMISSION_ID,ROLE_ID )
SELECT P.ID,2 FROM PERMISSIONS P
WHERE P.ID NOT IN (SELECT PERMISSION_ID FROM ROLES_PERMISSIONS WHERE ROLE_ID=2)
/
INSERT INTO ROLES_PERMISSIONS (PERMISSION_ID,ROLE_ID )
SELECT P.ID,3 FROM PERMISSIONS P
WHERE P.ID NOT IN (SELECT PERMISSION_ID FROM ROLES_PERMISSIONS WHERE ROLE_ID=3)
/
INSERT INTO ROLES_PERMISSIONS (PERMISSION_ID,ROLE_ID )
SELECT P.ID,4 FROM PERMISSIONS P
WHERE P.ID IN (17,18,20,21,22,28,29,30,31,32,33,37) AND P.ID NOT IN (SELECT PERMISSION_ID FROM ROLES_PERMISSIONS WHERE ROLE_ID=4)
/
INSERT INTO ROLES_PERMISSIONS (PERMISSION_ID,ROLE_ID )
SELECT P.ID,6 FROM PERMISSIONS P
WHERE P.ID IN (10,12,17,18,22,27,36) AND P.ID NOT IN (SELECT PERMISSION_ID FROM ROLES_PERMISSIONS WHERE ROLE_ID=6)
/
//FIN ROLES PERMISSIONS

//INICIO USERS ROLES
INSERT INTO USERS_ROLES (ROLE_ID,USER_ID)
VALUES (1,1)
/
INSERT INTO USERS_ROLES (ROLE_ID,USER_ID)
VALUES (2,2)
/
//FIN USERS ROLES

//INICIO TIPO DE FACTURA
INSERT INTO TIPO_FACTURA (ID, NOMBRE)
VALUES (1,'Anticipadas')
/
INSERT INTO TIPO_FACTURA (ID, NOMBRE)
VALUES (2,'Inmediatas')
/
INSERT INTO TIPO_FACTURA (ID, NOMBRE)
VALUES (3,'A vencer')
/       
//FIN TIPO DE FACTURA

