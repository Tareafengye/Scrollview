package com.scrollviewdemo.model;


import com.scrollviewdemo.net.BasicResponse;

import java.util.List;

/**
 * Created by Administrator on 2017/11/7 0007.
 */

public class MyIncomeBean extends BasicResponse {

    /**
     * data : {"list":[{"agencyId":4770,"change":2100,"createTime":1508306833000,"fkId":38597,"id":14068,"remark":"订单成交","reportId":2,"tag":1},{"agencyId":4770,"change":7500,"createTime":1506750250000,"fkId":35834,"id":13097,"remark":"订单成交","reportId":1,"tag":1},{"agencyId":4770,"change":7500,"createTime":1506741893000,"fkId":35448,"id":12928,"remark":"订单成交","reportId":1,"tag":1},{"agencyId":4770,"change":7500,"createTime":1506616836000,"fkId":35434,"id":12815,"remark":"订单成交","reportId":1,"tag":1},{"agencyId":4770,"change":7500,"createTime":1501576335000,"fkId":24434,"id":9614,"remark":"订单成交","reportId":11,"tag":1}]}
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private List<ListBean> list;

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
            /**
             * agencyId : 4770
             * change : 2100
             * createTime : 1508306833000
             * fkId : 38597
             * id : 14068
             * remark : 订单成交
             * reportId : 2
             * tag : 1
             */

            private String agencyId;
            private String change;
            private long createTime;
            private String fkId;
            private String id;
            private String remark;
            private String reportId;
            private String tag;

            public String getAgencyId() {
                return agencyId;
            }

            public void setAgencyId(String agencyId) {
                this.agencyId = agencyId;
            }

            public String getChange() {
                return change;
            }

            public void setChange(String change) {
                this.change = change;
            }

            public long getCreateTime() {
                return createTime;
            }

            public void setCreateTime(long createTime) {
                this.createTime = createTime;
            }

            public String getFkId() {
                return fkId;
            }

            public void setFkId(String fkId) {
                this.fkId = fkId;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getRemark() {
                return remark;
            }

            public void setRemark(String remark) {
                this.remark = remark;
            }

            public String getReportId() {
                return reportId;
            }

            public void setReportId(String reportId) {
                this.reportId = reportId;
            }

            public String getTag() {
                return tag;
            }

            public void setTag(String tag) {
                this.tag = tag;
            }
        }
    }
}
