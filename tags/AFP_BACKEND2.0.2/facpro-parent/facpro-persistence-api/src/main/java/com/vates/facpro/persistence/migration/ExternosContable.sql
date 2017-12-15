/* Verision 1.0.0*/

CREATE OR REPLACE VIEW "VIS_AUTORIZ_FAC_PROVEEDOR" ( "ID", "CUIT", "RAZON_SOCIAL", "NRO_FACTURA", "DESCRIPCION", "FECHA_VENCIMIENTO", "FECHA_CONTABLE", "FECHA_FACTURA", "GRAL_NETO", "GRAL_GRAV_IVA", "GRAL_NO_GRAV_IVA", "GRAL_IMP_INT", "GRAL_IVA_INC", "GRAL_IVA_NO_INC", "GRAL_BRUTO" )
AS
SELECT
        CGRAL.CGRAL_ID AS ID,
    PRO.PRO_NRO_CUIT CUIT,
        PRO.PRO_RAZSOC RAZON_SOCIAL,
        to_char(CGRAL.CGRAL_LETRA || '-' || LPAD(SUBSTR(TO_CHAR(CGRAL.CGRAL_NROSUC),
        1,4),4,'0') ||'-' || LPAD(SUBSTR(TO_CHAR(CGRAL.CGRAL_NROCOMP),1,8),8,'0') )
        NRO_FACTURA,
        CGRAL.CGRAL_OBSERVACION DESCRIPCION,
        CUO.CCUO_FEC_VENC FECHA_VENCIMIENTO,
        CGRAL.CGRAL_FECHA_CONT FECHA_CONTABLE,
        CGRAL.cgral_fecha_fact FECHA_FACTURA,
        CGRAL.cgral_neto,
        CGRAL.cgral_grav_iva,
        CGRAL.cgral_nograv_iva,
        CGRAL.cgral_impint,
        CGRAL.cgral_ivai,
        CGRAL.cgral_ivani,
        CGRAL.cgral_bruto 
       
FROM
        PROVEEDOR PRO
                INNER JOIN COMPGRAL CGRAL
                ON PRO.PRO_ID = CGRAL.PRO_ID AND
                CGRAL.CGRAL_BAJA = 'N'
                        INNER JOIN COMP_CUO CUO
                        ON CGRAL.CGRAL_ID = CUO.CGRAL_ID
                                INNER JOIN ASI_CAB ASI
                                ON CGRAL.CGRAL_ID = ASI.CGRAL_ID
WHERE
        CGRAL.EMP_ID = 1 AND CGRAL.cgral_fecha_fact >= to_date('01/01/2008','dd/mm/yyyy')
GROUP BY
    CGRAL.CGRAL_ID,
        PRO.PRO_NRO_CUIT,
        PRO.PRO_RAZSOC,
        CGRAL.CGRAL_LETRA,
        CGRAL.CGRAL_NROSUC,
        CGRAL.CGRAL_NROCOMP,
        CGRAL.CGRAL_OBSERVACION,
        CUO.CCUO_FEC_VENC,
        CGRAL.CGRAL_FECHA_CONT,
        CGRAL.cgral_fecha_fact,
        CGRAL.cgral_neto,
        CGRAL.cgral_grav_iva,
        CGRAL.cgral_nograv_iva,
        CGRAL.cgral_impint,
        CGRAL.cgral_ivai,
        CGRAL.cgral_ivani,
        CGRAL.cgral_bruto
        
 /
 
 CREATE OR REPLACE VIEW "VIS_CONTROL_GASTO" ( "ID", "NRO_CUENTA", "DESCRIPCION_CUENTA", "NRO_CTRO_CTOS", "DESCRIPCION_CTRO_CTOS", "IMPORTE_NETO" )
AS
SELECT
        CGRAL.CGRAL_ID AS ID,
        CECO.CTA_ID NRO_CUENTA,
        CTA.CTA_DESCRIP DESCRIPCION_CUENTA,
        CC.CC_ID NRO_CTRO_CTOS,
        CC.CC_NOMBRE DESCRIPCION_CTRO_CTOS,
        SUM(
        CASE
                WHEN RCECO.RASI_POSICION = 'D'
                THEN RCECO.IMPORTE
                ELSE RCECO.IMPORTE *-1
        END ) IMPORTE_NETO
FROM
        PROVEEDOR PRO
                INNER JOIN COMPGRAL CGRAL
                ON PRO.PRO_ID = CGRAL.PRO_ID AND
                CGRAL.CGRAL_BAJA = 'N'
                        INNER JOIN COMP_CUO CUO
                        ON CGRAL.CGRAL_ID = CUO.CGRAL_ID
                                INNER JOIN ASI_CAB ASI
                                ON CGRAL.CGRAL_ID = ASI.CGRAL_ID
                                        INNER JOIN ASIENTOS_CC CECO
                                        ON ASI.ASI_ID = CECO.ASIENTO_ID AND
                                        CECO.ORIGEN_ID = 'PRO' AND
                                        CECO.ASI_BAJA = 'N'
                                                INNER JOIN CUENTAS CTA
                                                ON CECO.CTA_ID = CTA.CTA_ID
                                                        INNER JOIN RASIENTOS_CC RCECO
                                                        ON CECO.ASIENTO_CC_ID = RCECO.ASIENTO_CC_ID
                                                                INNER JOIN AUX_CTRO_CTOS CC
                                                                ON RCECO.CC_ID = CC.CC_ID
WHERE
        CGRAL.EMP_ID = 1  AND CGRAL.cgral_fecha_fact >= to_date('01/01/2008','dd/mm/yyyy')
GROUP BY
    CGRAL.CGRAL_ID,
        CECO.CTA_ID,
        CTA.CTA_DESCRIP,
        CC.CC_ID,
        CC.CC_NOMBRE
        /
/* Verision 1.0.0*/
    
/* Verision 2.0.0*/
CREATE OR REPLACE VIEW "VIS_CONTROL_GASTO" ( "ID", "NRO_CUENTA", "DESCRIPCION_CUENTA", "NRO_CTRO_CTOS", "DESCRIPCION_CTRO_CTOS", "IMPORTE_NETO" , "JERARQUIA")
AS
SELECT
        CGRAL.CGRAL_ID AS ID,
        CECO.CTA_ID NRO_CUENTA,
        CTA.CTA_DESCRIP DESCRIPCION_CUENTA,
        CC.CC_ID NRO_CTRO_CTOS,
        CC.CC_NOMBRE DESCRIPCION_CTRO_CTOS,
        SUM(
        CASE
                WHEN RCECO.RASI_POSICION = 'D'
                THEN RCECO.IMPORTE
                ELSE RCECO.IMPORTE *-1
        END ) IMPORTE_NETO,
        CTA.CTA_JERARQUIA
FROM
        PROVEEDOR PRO
                INNER JOIN COMPGRAL CGRAL
                ON PRO.PRO_ID = CGRAL.PRO_ID AND
                CGRAL.CGRAL_BAJA = 'N'
                        INNER JOIN COMP_CUO CUO
                        ON CGRAL.CGRAL_ID = CUO.CGRAL_ID
                                INNER JOIN ASI_CAB ASI
                                ON CGRAL.CGRAL_ID = ASI.CGRAL_ID
                                        INNER JOIN ASIENTOS_CC CECO
                                        ON ASI.ASI_ID = CECO.ASIENTO_ID AND
                                        CECO.ORIGEN_ID = 'PRO' AND
                                        CECO.ASI_BAJA = 'N'
                                                INNER JOIN CUENTAS CTA
                                                ON CECO.CTA_ID = CTA.CTA_ID
                                                        INNER JOIN RASIENTOS_CC RCECO
                                                        ON CECO.ASIENTO_CC_ID = RCECO.ASIENTO_CC_ID
                                                                INNER JOIN AUX_CTRO_CTOS CC
                                                                ON RCECO.CC_ID = CC.CC_ID
WHERE
        CGRAL.EMP_ID = 1  AND CGRAL.cgral_fecha_fact >= to_date('01/01/2008','dd/mm/yyyy')
GROUP BY
    CGRAL.CGRAL_ID,
        CECO.CTA_ID,
        CTA.CTA_DESCRIP,
        CC.CC_ID,
        CC.CC_NOMBRE,
        CTA.CTA_JERARQUIA
union
SELECT
        CGRAL.CGRAL_ID AS ID,
        CTA.CTA_ID NRO_CUENTA,
        CTA.CTA_DESCRIP DESCRIPCION_CUENTA,
        0 AS NRO_CTRO_CTOS,
        '-' AS DESCRIPCION_CTRO_CTOS,
        0  AS IMPORTE_NETO,
        CTA.CTA_JERARQUIA
FROM
        PROVEEDOR PRO
                INNER JOIN COMPGRAL CGRAL
                ON PRO.PRO_ID = CGRAL.PRO_ID AND
                CGRAL.CGRAL_BAJA = 'N'
                        INNER JOIN COMP_CUO CUO
                        ON CGRAL.CGRAL_ID = CUO.CGRAL_ID
                                INNER JOIN ASI_CAB ASI
                                ON CGRAL.CGRAL_ID = ASI.CGRAL_ID
                                    INNER JOIN ASI_MOV AM 
                                    ON AM.ASI_ID=ASI.ASI_ID
                                        LEFT JOIN CUENTAS CTA
                                         ON AM.CTA_ID = CTA.CTA_ID
                                                       
WHERE
        CGRAL.EMP_ID = 1  AND CGRAL.cgral_fecha_fact >= to_date('01/01/2008','dd/mm/yyyy')

/      
CREATE OR REPLACE VIEW OC_MONEDA (ID, NOMBRE, SIMBOLO) AS
select MON_ID,MON_NOMBRE,SIMBOLO from MONEDA_CONTA
/
CREATE OR REPLACE VIEW OC_COMERCIAL_CLIENTE (CLI_ID, COMERCIAL) AS
select CLI.CLI_ID , rtrim (xmlagg (xmlelement(e ,COM.CRL_DESCRIPCION||', ')).extract ('//text()'), ', ')
from V_CLIENTE CLI INNER JOIN 
COMERCIAL_X_CLIENTE CXC ON CXC.CLI_ID=CLI.CLI_ID
INNER JOIN COMERCIALES COM ON CXC.CRL_ID = COM.CRL_ID
GROUP BY CLI.CLI_ID
/
grant select on OC_COMERCIAL_CLIENTE to prefa
/
/* Verision 2.0.0*/
/* Verision 2.0.1*/
DROP VIEW OC_COMERCIAL_CLIENTE
/
grant select on V_CLIENTE to prefa
/
grant select on COMERCIAL_X_CLIENTE to prefa
/
grant select on COMERCIALES to prefa
/
grant SELECT on v_talonario to prefa
/
/* Verision 2.0.1*/
