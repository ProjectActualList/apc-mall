<template>
  <div></div>
</template>

<script>
  export default {
    name: 'EnRegionPicker',
    props: {
      default: {
        default: null
      },
      api: {
        type: String,
        required: true
      }
    },
    watch: {
      default() {
        this.initAddressSelect()
      }
    },
    mounted() {
      this.initAddressSelect()
    },
    methods: {
      callback(object) {
        this.$emit('changed', object)
      },
      initAddressSelect() {
        this.$nextTick(() => {
          $(this.$el).addressSelect({
            api: this.api,
            callback: this.callback,
            deData: this.default
          })
        })
      }
    }
  }
</script>

<style type="text/scss" lang="scss" scoped>

</style>
