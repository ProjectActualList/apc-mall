import FloorTagsPicker from './FloorTagsPicker'
import FloorTitlePicker from './FloorTitlePicker'
import GradeEditor from './GradeEditor'
import ShopPicker from './ShopPicker'
import UE from './UE'

const components = {
  FloorTagsPicker,
  FloorTitlePicker,
  GradeEditor,
  ShopPicker
}

components.install = function(Vue) {
  Object.keys(components).forEach(key => {
    key !== 'install' && Vue.component(components[key].name, components[key])
  })
}

export default components

export {
  FloorTagsPicker,
  FloorTitlePicker,
  GradeEditor,
  ShopPicker,
  UE
}
