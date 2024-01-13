import { createSvgIconsPlugin } from 'vite-plugin-svg-icons'
import path from 'path'

export default function createSvgIcon(isBuild) {
    return createSvgIconsPlugin({
		iconDirs: [path.resolve(process.cwd(), 'src/assets/icons/svg'),
    path.resolve(process.cwd(), 'src/assets/icons/Buildings'),
    path.resolve(process.cwd(), 'src/assets/icons/Business'),
    path.resolve(process.cwd(), 'src/assets/icons/Device'),
    path.resolve(process.cwd(), 'src/assets/icons/Document'),
    path.resolve(process.cwd(), 'src/assets/icons/Others'),
    path.resolve(process.cwd(), 'src/assets/icons/System'),
    path.resolve(process.cwd(), 'src/assets/icons/User')
    ],
        symbolId: 'icon-[dir]-[name]',
        svgoOptions: isBuild
    })
}
