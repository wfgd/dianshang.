/**
 * Vue3 子组件定义
 * 包含：促销标签组件、倒计时组件、商品详情页组件
 */

// ========== 促销标签子组件 ==========
const PromotionTag = {
    name: 'PromotionTag',
    props: {
        type: {
            type: String,
            default: 'none'
        },
        discountRate: {
            type: Number,
            default: 1
        },
        description: {
            type: String,
            default: ''
        }
    },
    computed: {
        tagStyle() {
            const styles = {
                limited: { backgroundColor: '#f56c6c', color: '#fff' },
                discount: { backgroundColor: '#e6a23c', color: '#fff' },
                new: { backgroundColor: '#67c23a', color: '#fff' },
                hot: { backgroundColor: '#409eff', color: '#fff' },
                none: { backgroundColor: '#909399', color: '#fff' }
            };
            return styles[this.type] || styles.none;
        },
        tagText() {
            if (this.description) return this.description;
            if (this.type === 'discount') {
                return Math.round(this.discountRate * 10) + '折优惠';
            }
            const texts = {
                limited: '限时抢购',
                new: '新品上市',
                hot: '热销爆款',
                none: '普通商品'
            };
            return texts[this.type] || texts.none;
        },
        showTag() {
            return this.type !== 'none';
        }
    },
    template: `<span v-if="showTag" class="promotion-tag" :style="tagStyle">{{ tagText }}</span>`
};

// ========== 倒计时子组件 ==========
const CountdownTimer = {
    name: 'CountdownTimer',
    props: {
        endTime: {
            type: [Number, String, Date],
            required: true
        },
        showDays: {
            type: Boolean,
            default: true
        },
        endMessage: {
            type: String,
            default: '活动已结束'
        }
    },
    data() {
        return {
            remaining: 0,
            timer: null
        };
    },
    computed: {
        days() { return Math.floor(this.remaining / (1000 * 60 * 60 * 24)); },
        hours() { return Math.floor((this.remaining % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60)); },
        minutes() { return Math.floor((this.remaining % (1000 * 60 * 60)) / (1000 * 60)); },
        seconds() { return Math.floor((this.remaining % (1000 * 60)) / 1000); },
        formattedDays() { return String(this.days).padStart(2, '0'); },
        formattedHours() { return String(this.hours).padStart(2, '0'); },
        formattedMinutes() { return String(this.minutes).padStart(2, '0'); },
        formattedSeconds() { return String(this.seconds).padStart(2, '0'); },
        isEnded() { return this.remaining <= 0; }
    },
    mounted() {
        this.startCountdown();
    },
    beforeUnmount() {
        if (this.timer) clearInterval(this.timer);
    },
    watch: {
        endTime() {
            this.startCountdown();
        }
    },
    methods: {
        startCountdown() {
            if (this.timer) clearInterval(this.timer);
            
            let endTimestamp;
            if (typeof this.endTime === 'number') {
                endTimestamp = this.endTime;
            } else if (typeof this.endTime === 'string') {
                endTimestamp = new Date(this.endTime).getTime();
            } else if (this.endTime instanceof Date) {
                endTimestamp = this.endTime.getTime();
            } else {
                return;
            }
            
            this.updateRemaining(endTimestamp);
            this.timer = setInterval(() => {
                this.updateRemaining(endTimestamp);
            }, 1000);
        },
        updateRemaining(endTimestamp) {
            const now = Date.now();
            this.remaining = Math.max(0, endTimestamp - now);
            
            if (this.remaining <= 0 && this.timer) {
                clearInterval(this.timer);
                this.timer = null;
                this.$emit('ended');
            }
        }
    },
    template: `<div class="countdown-timer">
        <div v-if="isEnded" class="countdown-ended">{{ endMessage }}</div>
        <div v-else class="countdown-display">
            <span v-if="showDays && days > 0" class="countdown-item">
                <span class="countdown-value">{{ formattedDays }}</span>
                <span class="countdown-unit">天</span>
            </span>
            <span class="countdown-item">
                <span class="countdown-value">{{ formattedHours }}</span>
                <span class="countdown-unit">时</span>
            </span>
            <span class="countdown-item">
                <span class="countdown-value">{{ formattedMinutes }}</span>
                <span class="countdown-unit">分</span>
            </span>
            <span class="countdown-item">
                <span class="countdown-value">{{ formattedSeconds }}</span>
                <span class="countdown-unit">秒</span>
            </span>
        </div>
    </div>`
};

// 导出组件到全局
window.PromotionTag = PromotionTag;
window.CountdownTimer = CountdownTimer;