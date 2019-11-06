export default function(options) {
  return {
    title: {
      text: options.titleText || '区域分析',
      left: 'center'
    },
    tooltip: {
      trigger: 'item'
    },
    visualMap: {
      min: 0,
      max: options.visualMapMax || 2500,
      left: 'left',
      top: 'bottom',
      text: ['高', '低'],
      calculable: true
    },
    toolbox: {
      show: true,
      orient: 'vertical',
      left: 'right',
      top: 'center',
      feature: {
        dataView: { readOnly: false }
      }
    },
    series: [
      {
        type: 'map',
        mapType: 'china',
        roam: false,
        name: options.seriesName || '数量',
        label: {
          normal: {
            show: true
          },
          emphasis: {
            show: true
          }
        },
        data: options.seriesData || []
      }
    ]
  }
}
