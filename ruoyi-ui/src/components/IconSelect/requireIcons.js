// let icons = []
// const modules = import.meta.glob('./../../assets/icons/svg/*.svg');
// for (const path in modules) {
//   const p = path.split('assets/icons/svg/')[1].split('.svg')[0];
//   icons.push(p);
// }

// export default icons



// const req = require.context('../../assets/icons/svg', false, /\.svg$/)
// const requireAll = requireContext => requireContext.keys()

// const re = /\.\/(.*)\.svg/

// const icons = requireAll(req).map(i => {
//   return i.match(re)[1]
// })

// export default icons




const Base = import.meta.glob('./../../assets/icons/svg/*.svg')
const Buildings = import.meta.glob('./../../assets/icons/Buildings/*.svg')
const Business = import.meta.glob('./../../assets/icons/Business/*.svg')
const Device = import.meta.glob('./../../assets/icons/Device/*.svg')
const Document = import.meta.glob('./../../assets/icons/Document/*.svg')
const Others = import.meta.glob('./../../assets/icons/Others/*.svg')
const System = import.meta.glob('./../../assets/icons/System/*.svg')
const User = import.meta.glob('./../../assets/icons/User/*.svg')

let BaseIcons = []
for (const path in Base) {
  const p = path.split('assets/icons/svg/')[1].split('.svg')[0];
  BaseIcons.push(p);
}

let BuildingsIcons = []
for (const path in Buildings) {
  const p = path.split('assets/icons/Buildings/')[1].split('.svg')[0];
  BuildingsIcons.push(p);
}

let BusinessIcons = []
for (const path in Business) {
  const p = path.split('assets/icons/Business/')[1].split('.svg')[0];
  BusinessIcons.push(p);
}

let DeviceIcons = []
for (const path in Device) {
  const p = path.split('assets/icons/Device/')[1].split('.svg')[0];
  DeviceIcons.push(p);
}

let DocumentIcons = []
for (const path in Document) {
  const p = path.split('assets/icons/Document/')[1].split('.svg')[0];
  DocumentIcons.push(p);
}

let OthersIcons = []
for (const path in Others) {
  const p = path.split('assets/icons/Others/')[1].split('.svg')[0];
  OthersIcons.push(p);
}

let SystemIcons = []
for (const path in System) {
  const p = path.split('assets/icons/System/')[1].split('.svg')[0];
  SystemIcons.push(p);
}

let UserIcons = []
for (const path in User) {
  const p = path.split('assets/icons/User/')[1].split('.svg')[0];
  UserIcons.push(p);
}

const icons = [
  {
    classifyName:'用户',
    iconList:UserIcons
  },
  {
    classifyName:'建筑',
    iconList:BuildingsIcons
  },
  {
    classifyName:'办公',
    iconList:BusinessIcons
  },
  {
    classifyName:'设备',
    iconList:DeviceIcons
  },
  {
    classifyName:'文档',
    iconList:DocumentIcons
  },
  {
    classifyName:'系统',
    iconList:SystemIcons
  },
  {
    classifyName:'其他',
    iconList:OthersIcons
  },
  {
    classifyName:'默认',
    iconList:BaseIcons
  }
]

export default icons
