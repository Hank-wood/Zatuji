package com.joe.huaban.beauty.model;

import java.util.List;

/**
 * Created by Joe on 2016/4/8.
 */
public class BeautyData {
    /**
     * pin_id : 676909879
     * user_id : 14788852
     * board_id : 16780421
     * file_id : 58539420
     * file : {"farm":"farm1","bucket":"hbimg","key":"e5a3563866f9ff795b22ebd9d0eb6606eea92065e0c9-cc52b1","type":"image/jpeg","width":650,"height":837,"frames":1}
     * media_type : 0
     * source : 58pic.com
     * link : http://www.58pic.com/haibao/12570818.html
     * raw_text : 电影《我的女友是机器人》
     * text_meta : {}
     * via : 245072748
     * via_user_id : 15546103
     * original : 245072748
     * created_at : 1460127495
     * like_count : 0
     * comment_count : 0
     * repin_count : 0
     * is_public : 0
     * orig_source : null
     * user : {"user_id":14788852,"username":"嘉陵渔翁","urlname":"lueyangyu","created_at":1407405175,"avatar":{"id":49010053,"farm":"farm1","bucket":"hbimg","key":"22d7ad37bf2f9c7a272bbe23336ad987075ebb6a1e53-PdDNjE","type":"image/jpeg","width":100,"height":100,"frames":1}}
     * board : {"board_id":16780421,"user_id":14788852,"title":"颜如舜华","description":"","category_id":"beauty","seq":1,"pin_count":207,"follow_count":128,"like_count":1,"created_at":1407404991,"updated_at":1460127495,"deleting":0,"is_public":0,"extra":null}
     * via_user : {"user_id":15546103,"username":"小小小包子","urlname":"whlhmpplqj","created_at":1411626944,"avatar":{"id":40426284,"farm":"farm1","bucket":"hbimg","key":"dbc6fd49f1915545cc42c1a1492a418dbaebd2c21bb9-9aDqgl","type":"image/gif","width":100,"height":100,"frames":1}}
     */

    public List<PinsBean> pins;

    public List<PinsBean> getPins() {
        return pins;
    }

    public void setPins(List<PinsBean> pins) {
        this.pins = pins;
    }

    public static class PinsBean {
        public int pin_id;
        public int user_id;
        public int board_id;
        public int file_id;
        /**
         * farm : farm1
         * bucket : hbimg
         * key : e5a3563866f9ff795b22ebd9d0eb6606eea92065e0c9-cc52b1
         * type : image/jpeg
         * width : 650
         * height : 837
         * frames : 1
         */

        public FileBean file;
        public int media_type;
        public String source;
        public String link;
        public String raw_text;
        public int via;
        public int via_user_id;
        public int original;
        public int created_at;
        public int like_count;
        public int comment_count;
        public int repin_count;
        public int is_public;
        public Object orig_source;

        public int getPin_id() {
            return pin_id;
        }

        public void setPin_id(int pin_id) {
            this.pin_id = pin_id;
        }

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

        public int getBoard_id() {
            return board_id;
        }

        public void setBoard_id(int board_id) {
            this.board_id = board_id;
        }

        public int getFile_id() {
            return file_id;
        }

        public void setFile_id(int file_id) {
            this.file_id = file_id;
        }

        public FileBean getFile() {
            return file;
        }

        public void setFile(FileBean file) {
            this.file = file;
        }

        public int getMedia_type() {
            return media_type;
        }

        public void setMedia_type(int media_type) {
            this.media_type = media_type;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public String getRaw_text() {
            return raw_text;
        }

        public void setRaw_text(String raw_text) {
            this.raw_text = raw_text;
        }

        public int getVia() {
            return via;
        }

        public void setVia(int via) {
            this.via = via;
        }

        public int getVia_user_id() {
            return via_user_id;
        }

        public void setVia_user_id(int via_user_id) {
            this.via_user_id = via_user_id;
        }

        public int getOriginal() {
            return original;
        }

        public void setOriginal(int original) {
            this.original = original;
        }

        public int getCreated_at() {
            return created_at;
        }

        public void setCreated_at(int created_at) {
            this.created_at = created_at;
        }

        public int getLike_count() {
            return like_count;
        }

        public void setLike_count(int like_count) {
            this.like_count = like_count;
        }

        public int getComment_count() {
            return comment_count;
        }

        public void setComment_count(int comment_count) {
            this.comment_count = comment_count;
        }

        public int getRepin_count() {
            return repin_count;
        }

        public void setRepin_count(int repin_count) {
            this.repin_count = repin_count;
        }

        public int getIs_public() {
            return is_public;
        }

        public void setIs_public(int is_public) {
            this.is_public = is_public;
        }

        public Object getOrig_source() {
            return orig_source;
        }

        public void setOrig_source(Object orig_source) {
            this.orig_source = orig_source;
        }

        public static class FileBean {
            public String farm;
            public String bucket;
            public String key;
            public String type;
            public int width;
            public int height;
            public int frames;

            public String getFarm() {
                return farm;
            }

            public void setFarm(String farm) {
                this.farm = farm;
            }

            public String getBucket() {
                return bucket;
            }

            public void setBucket(String bucket) {
                this.bucket = bucket;
            }

            public String getKey() {
                return key;
            }

            public void setKey(String key) {
                this.key = key;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public int getWidth() {
                return width;
            }

            public void setWidth(int width) {
                this.width = width;
            }

            public int getHeight() {
                return height;
            }

            public void setHeight(int height) {
                this.height = height;
            }

            public int getFrames() {
                return frames;
            }

            public void setFrames(int frames) {
                this.frames = frames;
            }
        }
    }
}
