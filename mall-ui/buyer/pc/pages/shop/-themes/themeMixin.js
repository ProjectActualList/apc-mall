import Vue from 'vue'
import * as API_Shop from '@/api/shop'
import * as API_Goods from '@/api/goods'
import * as API_Members from '@/api/members'
import ShopSildes from './-shop-sildes'
import ShopStar from './-shop-star'

export default {
  data() {
    return {
      shop_id: this.$route.params.id,
      categorys: [],
      // 热销商品
      hotGoods: [],
      // 新品上架
      newGoods: [],
      // 推荐商品
      recGoods: []
    }
  },
  props: {
    shop: {
      type: Object,
      default: () => ({})
    }
  },
  components: {
    'en-shop-sildes': ShopSildes,
    'en-shop-star': ShopStar
  },
  mounted() {
    this.getShopTagGoods()
    this.goToAnchor()
  },
  watch: {
    $route: 'goToAnchor'
  },
  methods: {
    /** 去描点处 */
    goToAnchor() {
      const { hash } = location
      if (hash === '#shop-intro' || hash === '#shop-info') {
        $('html,body').animate({ scrollTop: $(hash).offset().top }, 300)
      }
    },
    /** 格式化店铺地址 */
    formatAddress() {
      const { shop_province, shop_city, shop_county, shop_town } = this.shop
      return `${shop_province}-${shop_city}${shop_county ? '-' + shop_county : ''}${shop_town ? '-' + shop_town : ''}`
    },
    /** 收藏店铺 */
    collectionShop() {
      if (!this.$store.getters.user) {
        this.$message.error('未登录不能收藏店铺！')
        return false
      }
      API_Members.collectionShop(this.shop_id).then(() => {
        this.$message.success('收藏成功！')
      })
    },
    /** 获取店铺标签商品 */
    getShopTagGoods() {
      Promise.all([
        API_Goods.getTagGoods(this.shop_id, 'hot', 10),
        API_Goods.getTagGoods(this.shop_id, 'new', 10),
        API_Goods.getTagGoods(this.shop_id, 'recommend', 10),
      ]).then(responses => {
        this.hotGoods = responses[0]
        this.newGoods = responses[1]
        this.recGoods = responses[2]
      })
    }
  }
}
