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
    .binaryTarget(name: "AVPlayerDataSDK", url: "https://github.com/sportradar/SRPlayer-SDK-Core/releases/download/v0.1.0.486/AVPlayerDataSDK.xcframework.zip", checksum: "fbac91bb23be2bfae61a0e4ad837e2bef96cfad30b4345ff6a62deff56a7f893"),
  ]
)
