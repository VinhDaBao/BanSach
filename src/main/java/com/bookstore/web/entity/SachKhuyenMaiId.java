package com.bookstore.web.entity;

import java.io.Serializable;

public class SachKhuyenMaiId implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer maSach;
    private Integer maKM;
	public SachKhuyenMaiId(Integer maSach, Integer maKM) {
		this.maSach = maSach;
		this.maKM = maKM;
	}
	public SachKhuyenMaiId() {
    }
    

}