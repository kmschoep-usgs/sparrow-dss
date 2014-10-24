OPTIONS (SKIP=1)
LOAD DATA
INTO TABLE TEMP_ANCIL
TRUNCATE
FIELDS TERMINATED BY X'9'
TRAILING NULLCOLS
(local_id
,std_id
,new_or_modified
,mrb_id
,hydseq "nvl(:hydseq,0)"
,sqkm
,demtarea
,meanq
,delivery_target filler
,huc8 "substr(:huc8,1,8)"
,CONTFLAG filler
,PNAME
,HEADFLAG
,TERMFLAG
,RESCODE filler
,RCHTYPE
,station_id filler
,est_cd filler
,regab filler
,statid filler
,del_frac filler
,mean_del_frac filler
,se_del_frac filler
,ci_lo_del_frac filler
,ci_hi_del_frac filler
)
