package com.merkle.wechat.vo.statistics;

public class FollowerStatisticsVo {
    private Item today = new Item();
    private Item yesterday = new Item();

    public Item getToday() {
        return today;
    }

    public void setToday(Item today) {
        this.today = today;
    }

    public Item getYesterday() {
        return yesterday;
    }

    public void setYesterday(Item yesterday) {
        this.yesterday = yesterday;
    }

    public class Item {
        private Long subscribeCount = 0L;
        private Long unsubscribeCount = 0L;
        private Long increaseCount = 0L;
        private Long totalCount = 0L;

        public Long getSubscribeCount() {
            return subscribeCount;
        }

        public void setSubscribeCount(Long subscribeCount) {
            this.subscribeCount = subscribeCount;
        }

        public Long getUnsubscribeCount() {
            return unsubscribeCount;
        }

        public void setUnsubscribeCount(Long unsubscribeCount) {
            this.unsubscribeCount = unsubscribeCount;
        }

        public Long getIncreaseCount() {
            return increaseCount;
        }

        public void setIncreaseCount(Long increaseCount) {
            this.increaseCount = increaseCount;
        }

        public Long getTotalCount() {
            return totalCount;
        }

        public void setTotalCount(Long totalCount) {
            this.totalCount = totalCount;
        }
    }

}
