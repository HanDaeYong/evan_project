package kr.co.skmns.trafic.scheduler.dao;

import java.util.List;

import kr.co.skmns.trafic.scheduler.model.UBISKrentcarBean;

public abstract interface TraficBatchDao {
	
	//렌트카 일배치용 SELECT
	//public List<UBISKrentcarBean> selectRegYesterdayList();
	public List<UBISKrentcarBean> selectRegYesterdayList();
	public String testDbCon();

}