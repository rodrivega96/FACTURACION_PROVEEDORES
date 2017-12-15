
/* Verision 2.0.0*/
        
create or replace synonym OC_COMERCIAL_CLIENTE for CONTABLE.OC_COMERCIAL_CLIENTE
/
        
CREATE OR REPLACE VIEW OC_CLIENTE (ID, NOMBRE, CONTACTO) AS
select CLI_ID, CLI_RAZSOC,CLI_CONTACTO from V_CLIENTE
/

CREATE OR REPLACE VIEW OC_PROYECTO (ID, PM_ID, PM, PMG_ID, PMG, NOMBRE, ACTIVO, CLIENTE_ID) AS
select  PR.PRY_ID, PR.LEG_USUARIOALTA,upper(EMP.EPD_APELLIDO)||', '||upper(EMP.EPD_NOMBRE), 
MPP.LEG_ID , upper(EMP1.EPD_APELLIDO)||', '||upper(EMP1.EPD_NOMBRE),
PR.PRY_NOMBRE, PR.PRY_ACTIVO , PR.CLI_ID from PROYECTO PR 
INNER JOIN LEGAJOS LEG ON LEG.LEG_ID = PR.LEG_USUARIOALTA
INNER JOIN EMPLEADOS EMP ON EMP.EPD_ID = LEG.EPD_ID
LEFT JOIN MANAGER_POR_PROYECTO MPP ON MPP.PRY_ID=PR.PRY_ID AND MPP.MPP_RESPONSABLE=1
LEFT JOIN LEGAJOS LEG1 ON LEG1.LEG_ID = MPP.LEG_ID
LEFT JOIN EMPLEADOS EMP1 ON EMP1.EPD_ID = LEG1.EPD_ID
WHERE MPP.MPP_ID = (SELECT MAX (MPP1.MPP_ID) FROM MANAGER_POR_PROYECTO MPP1 WHERE 
MPP1.PRY_ID=PR.PRY_ID AND MPP1.MPP_RESPONSABLE=1) or MPP.LEG_ID is null
/
CREATE OR REPLACE VIEW OC_PROYECTO_PM (ID, PROYECTO_ID, PM, RESPONSABLE, ID_PM) AS
select  MPP.MPP_ID, MPP.PRY_ID,upper(EMP.EPD_APELLIDO)||', '||upper(EMP.EPD_NOMBRE), MPP.mpp_responsable , MPP.LEG_ID
from MANAGER_POR_PROYECTO MPP
INNER JOIN LEGAJOS LEG ON LEG.LEG_ID = MPP.LEG_ID
INNER JOIN EMPLEADOS EMP ON EMP.EPD_ID = LEG.EPD_ID
/
CREATE OR REPLACE VIEW OC_PROPUESTA (ID,COMERCIAL,DESCRIPCION, VIGENTE_DESDE, VIGENTE_HASTA, CLIENTE_ID) AS
select PC.PCM_ID,CC.COMERCIAL,
PC.PCM_DESCRIPCION,PC.PCM_FECHAVIGENCIAHASTA,PC.PCM_FECHAVIGENCIADESDE , PC.CLI_ID
from PROPUESTA_COMERCIAL PC INNER JOIN OC_COMERCIAL_CLIENTE CC ON PC.CLI_ID=CC.CLI_ID
/
CREATE OR REPLACE VIEW OC_PROYECTO_PROPUESTA (PROYECTO_ID, PROPUESTA_ID) AS
select PRY_ID, PCM_ID from PROYECTO_POR_PROPUESTA
/
CREATE OR REPLACE VIEW OC_ESTADO_PREFA (ID, NOMBRE) AS
select EPF_ID, EPF_DESCRIPCION  from ESTADOS_PREFACTURACION
/
CREATE OR REPLACE VIEW OC_PREFACTURACION (ID,PROYECTO_ID,ESTADO_ID, DESCRIPCION, PERIODO , SALDO , CLIENTE_ID ,PM, PM_ID) AS
select PRE.PFR_ID,PRE.PRY_ID,PRE.EPF_ID, PRE.PFR_NOMBRE, PRE.PFR_PERIODO , 
PRE.PFR_SALDO , PRO.CLI_ID ,
upper(EMP.EPD_APELLIDO)||', '||upper(EMP.EPD_NOMBRE), PRO.LEG_USUARIOALTA
from PREFACTURACION PRE INNER JOIN PROYECTO PRO ON PRO.PRY_ID=PRE.PRY_ID
INNER JOIN LEGAJOS LEG ON LEG.LEG_ID = PRO.LEG_USUARIOALTA
INNER JOIN EMPLEADOS EMP ON EMP.EPD_ID = LEG.EPD_ID
/
CREATE OR REPLACE VIEW OC_FACTURA (ID ,CLIENTE_ID, NUMERO, TOTAL, NETO ) AS
SELECT COM.COM_NUMERO, COM.CLI_ID, 
COM.COM_LETRA|| '-'  || lpad(TAL.PV_ID,4,'0') || '-' || lpad(COM.COM_NUMERO,8,'0'),
COM.COM_BRUTO, COM.COM_NETO 
FROM V_COMPROBANTE COM INNER JOIN V_TALONARIO  TAL ON TAL.TAL_ID = COM.TAL_ID
/
CREATE OR REPLACE VIEW OC_MANAGERS (ID, NOMBRE) AS
SELECT DISTINCT LEG.LEG_ID ,upper(EMP.EPD_APELLIDO)||', '||upper(EMP.EPD_NOMBRE) 
FROM LEGAJOS LEG INNER JOIN EMPLEADOS EMP ON LEG.EPD_ID=EMP.EPD_ID
WHERE LEG.LEG_ID IN (SELECT DISTINCT LEG_ID FROM MANAGER_POR_PROYECTO)
OR LEG.LEG_ID IN (SELECT DISTINCT LEG_USUARIOALTA FROM PROYECTO)
/
CREATE OR REPLACE VIEW OC_PEOPLES (ID, PREFA_ID, PERSONA, IMPORTE) AS
SELECT DPF.DPF_ID , DPF.PFR_ID ,upper(EMP.EPD_APELLIDO)||', '||upper(EMP.EPD_NOMBRE), DPF.DPF_PRECIO
FROM DETALLE_PROYECTO DP INNER JOIN DETALLE_PREFACTURACION DPF ON DP.DPF_ID=DPF.DPF_ID
INNER JOIN LEGAJOS LEG 
ON DP.LEG_RECURSOID=LEG.LEG_ID
INNER JOIN EMPLEADOS EMP ON LEG.EPD_ID=EMP.EPD_ID
/
/* Verision 2.0.0*/
