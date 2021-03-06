package com.example.sumec.wash.model;

import java.util.List;

/**
 * Created by zhdk on 2019/1/22.
 */

public class WeatherBean {

    private List<HeWeather6Bean> HeWeather6;

    public List<HeWeather6Bean> getHeWeather6() {
        return HeWeather6;
    }

    public void setHeWeather6(List<HeWeather6Bean> HeWeather6) {
        this.HeWeather6 = HeWeather6;
    }

    public static class HeWeather6Bean {
        /**
         * basic : {"cid":"CN101190101","location":"南京","parent_city":"南京","admin_area":"江苏","cnty":"中国","lat":"32.04154587","lon":"118.76741028","tz":"+8.00"}
         * update : {"loc":"2019-01-22 09:57","utc":"2019-01-22 01:57"}
         * status : ok
         * daily_forecast : [{"cond_code_d":"100","cond_code_n":"100","cond_txt_d":"晴","cond_txt_n":"晴","date":"2019-01-22","hum":"58","mr":"18:46","ms":"07:50","pcpn":"0.0","pop":"0","pres":"1025","sr":"07:03","ss":"17:30","tmp_max":"10","tmp_min":"-1","uv_index":"6","vis":"20","wind_deg":"276","wind_dir":"西风","wind_sc":"3-4","wind_spd":"20"},{"cond_code_d":"100","cond_code_n":"100","cond_txt_d":"晴","cond_txt_n":"晴","date":"2019-01-23","hum":"63","mr":"19:56","ms":"08:39","pcpn":"0.0","pop":"0","pres":"1025","sr":"07:03","ss":"17:31","tmp_max":"13","tmp_min":"1","uv_index":"6","vis":"20","wind_deg":"-1","wind_dir":"无持续风向","wind_sc":"1-2","wind_spd":"11"},{"cond_code_d":"100","cond_code_n":"101","cond_txt_d":"晴","cond_txt_n":"多云","date":"2019-01-24","hum":"67","mr":"21:05","ms":"09:23","pcpn":"0.0","pop":"0","pres":"1026","sr":"07:02","ss":"17:32","tmp_max":"12","tmp_min":"1","uv_index":"6","vis":"20","wind_deg":"46","wind_dir":"东北风","wind_sc":"3-4","wind_spd":"14"},{"cond_code_d":"104","cond_code_n":"100","cond_txt_d":"阴","cond_txt_n":"晴","date":"2019-01-25","hum":"79","mr":"22:11","ms":"10:02","pcpn":"0.0","pop":"5","pres":"1027","sr":"07:02","ss":"17:33","tmp_max":"8","tmp_min":"0","uv_index":"2","vis":"20","wind_deg":"-1","wind_dir":"无持续风向","wind_sc":"1-2","wind_spd":"1"},{"cond_code_d":"100","cond_code_n":"100","cond_txt_d":"晴","cond_txt_n":"晴","date":"2019-01-26","hum":"55","mr":"23:15","ms":"10:39","pcpn":"0.0","pop":"3","pres":"1037","sr":"07:01","ss":"17:33","tmp_max":"6","tmp_min":"-1","uv_index":"3","vis":"20","wind_deg":"-1","wind_dir":"无持续风向","wind_sc":"1-2","wind_spd":"6"},{"cond_code_d":"100","cond_code_n":"305","cond_txt_d":"晴","cond_txt_n":"小雨","date":"2019-01-27","hum":"54","mr":"00:00","ms":"11:15","pcpn":"0.0","pop":"1","pres":"1034","sr":"07:01","ss":"17:34","tmp_max":"7","tmp_min":"0","uv_index":"4","vis":"20","wind_deg":"206","wind_dir":"西南风","wind_sc":"1-2","wind_spd":"11"},{"cond_code_d":"101","cond_code_n":"101","cond_txt_d":"多云","cond_txt_n":"多云","date":"2019-01-28","hum":"54","mr":"00:17","ms":"11:51","pcpn":"0.0","pop":"25","pres":"1028","sr":"07:00","ss":"17:35","tmp_max":"7","tmp_min":"1","uv_index":"1","vis":"20","wind_deg":"26","wind_dir":"东北风","wind_sc":"1-2","wind_spd":"5"}]
         */

        private BasicBean basic;
        private UpdateBean update;
        private String status;
        private List<DailyForecastBean> daily_forecast;

        public BasicBean getBasic() {
            return basic;
        }

        public void setBasic(BasicBean basic) {
            this.basic = basic;
        }

        public UpdateBean getUpdate() {
            return update;
        }

        public void setUpdate(UpdateBean update) {
            this.update = update;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public List<DailyForecastBean> getDaily_forecast() {
            return daily_forecast;
        }

        public void setDaily_forecast(List<DailyForecastBean> daily_forecast) {
            this.daily_forecast = daily_forecast;
        }

        public static class BasicBean {
            /**
             * cid : CN101190101
             * location : 南京
             * parent_city : 南京
             * admin_area : 江苏
             * cnty : 中国
             * lat : 32.04154587
             * lon : 118.76741028
             * tz : +8.00
             */

            private String cid;
            private String location;
            private String parent_city;
            private String admin_area;
            private String cnty;
            private String lat;
            private String lon;
            private String tz;

            public String getCid() {
                return cid;
            }

            public void setCid(String cid) {
                this.cid = cid;
            }

            public String getLocation() {
                return location;
            }

            public void setLocation(String location) {
                this.location = location;
            }

            public String getParent_city() {
                return parent_city;
            }

            public void setParent_city(String parent_city) {
                this.parent_city = parent_city;
            }

            public String getAdmin_area() {
                return admin_area;
            }

            public void setAdmin_area(String admin_area) {
                this.admin_area = admin_area;
            }

            public String getCnty() {
                return cnty;
            }

            public void setCnty(String cnty) {
                this.cnty = cnty;
            }

            public String getLat() {
                return lat;
            }

            public void setLat(String lat) {
                this.lat = lat;
            }

            public String getLon() {
                return lon;
            }

            public void setLon(String lon) {
                this.lon = lon;
            }

            public String getTz() {
                return tz;
            }

            public void setTz(String tz) {
                this.tz = tz;
            }
        }

        public static class UpdateBean {
            /**
             * loc : 2019-01-22 09:57
             * utc : 2019-01-22 01:57
             */

            private String loc;
            private String utc;

            public String getLoc() {
                return loc;
            }

            public void setLoc(String loc) {
                this.loc = loc;
            }

            public String getUtc() {
                return utc;
            }

            public void setUtc(String utc) {
                this.utc = utc;
            }
        }

        public static class DailyForecastBean {
            /**
             * cond_code_d : 100
             * cond_code_n : 100
             * cond_txt_d : 晴
             * cond_txt_n : 晴
             * date : 2019-01-22
             * hum : 58
             * mr : 18:46
             * ms : 07:50
             * pcpn : 0.0
             * pop : 0
             * pres : 1025
             * sr : 07:03
             * ss : 17:30
             * tmp_max : 10
             * tmp_min : -1
             * uv_index : 6
             * vis : 20
             * wind_deg : 276
             * wind_dir : 西风
             * wind_sc : 3-4
             * wind_spd : 20
             */

            private String cond_code_d;
            private String cond_code_n;
            private String cond_txt_d;
            private String cond_txt_n;
            private String date;
            private String hum;
            private String mr;
            private String ms;
            private String pcpn;
            private String pop;
            private String pres;
            private String sr;
            private String ss;
            private String tmp_max;
            private String tmp_min;
            private String uv_index;
            private String vis;
            private String wind_deg;
            private String wind_dir;
            private String wind_sc;
            private String wind_spd;

            public String getCond_code_d() {
                return cond_code_d;
            }

            public void setCond_code_d(String cond_code_d) {
                this.cond_code_d = cond_code_d;
            }

            public String getCond_code_n() {
                return cond_code_n;
            }

            public void setCond_code_n(String cond_code_n) {
                this.cond_code_n = cond_code_n;
            }

            public String getCond_txt_d() {
                return cond_txt_d;
            }

            public void setCond_txt_d(String cond_txt_d) {
                this.cond_txt_d = cond_txt_d;
            }

            public String getCond_txt_n() {
                return cond_txt_n;
            }

            public void setCond_txt_n(String cond_txt_n) {
                this.cond_txt_n = cond_txt_n;
            }

            public String getDate() {
                return date;
            }

            public void setDate(String date) {
                this.date = date;
            }

            public String getHum() {
                return hum;
            }

            public void setHum(String hum) {
                this.hum = hum;
            }

            public String getMr() {
                return mr;
            }

            public void setMr(String mr) {
                this.mr = mr;
            }

            public String getMs() {
                return ms;
            }

            public void setMs(String ms) {
                this.ms = ms;
            }

            public String getPcpn() {
                return pcpn;
            }

            public void setPcpn(String pcpn) {
                this.pcpn = pcpn;
            }

            public String getPop() {
                return pop;
            }

            public void setPop(String pop) {
                this.pop = pop;
            }

            public String getPres() {
                return pres;
            }

            public void setPres(String pres) {
                this.pres = pres;
            }

            public String getSr() {
                return sr;
            }

            public void setSr(String sr) {
                this.sr = sr;
            }

            public String getSs() {
                return ss;
            }

            public void setSs(String ss) {
                this.ss = ss;
            }

            public String getTmp_max() {
                return tmp_max;
            }

            public void setTmp_max(String tmp_max) {
                this.tmp_max = tmp_max;
            }

            public String getTmp_min() {
                return tmp_min;
            }

            public void setTmp_min(String tmp_min) {
                this.tmp_min = tmp_min;
            }

            public String getUv_index() {
                return uv_index;
            }

            public void setUv_index(String uv_index) {
                this.uv_index = uv_index;
            }

            public String getVis() {
                return vis;
            }

            public void setVis(String vis) {
                this.vis = vis;
            }

            public String getWind_deg() {
                return wind_deg;
            }

            public void setWind_deg(String wind_deg) {
                this.wind_deg = wind_deg;
            }

            public String getWind_dir() {
                return wind_dir;
            }

            public void setWind_dir(String wind_dir) {
                this.wind_dir = wind_dir;
            }

            public String getWind_sc() {
                return wind_sc;
            }

            public void setWind_sc(String wind_sc) {
                this.wind_sc = wind_sc;
            }

            public String getWind_spd() {
                return wind_spd;
            }

            public void setWind_spd(String wind_spd) {
                this.wind_spd = wind_spd;
            }
        }
    }
}
