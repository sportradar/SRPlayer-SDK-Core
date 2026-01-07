// swift-tools-version:6.0
import PackageDescription
let package = Package(
  name: "AVPlayerDataSDK",
  platforms: [
    .iOS(.v16),.tvOS(.v16),.macOS(.v14),.visionOS(.v1)
  ],
  products: [
    .library(name: "AVPlayerDataSDK", targets: [ "AVPlayerDataSDK" ]),
  ],
  targets: [
    .binaryTarget(name: "AVPlayerDataSDK", url: "https://github.com/sportradar/SRPlayer-SDK-Core/releases/download/v0.1.0/AVPlayerDataSDK.xcframework.zip", checksum: "5a470fbb8582bfb86eeb63c38f4db889d6d9df2298b3288eb25eb9579a76060b"),
  ]
)
