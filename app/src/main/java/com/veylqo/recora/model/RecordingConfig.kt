package com.veylqo.recora.model

enum class AudioOption(val label: String) {
    INTERNAL("Internal"),
    MIC("Microphone"),
    MUTE("Mute (Off)")
}

enum class QualityOption(val label: String, val badge: String, val width: Int, val height: Int, val fps: Int, val bitrate: Int) {
    UHD_4K("4K UHD • 60fps", "4K", 3840, 2160, 60, 20_000_000),
    QHD_2K("2K UHD • 60fps", "2K", 2560, 1440, 60, 12_000_000),
    FHD_1080P("FHD • 60fps", "FHD", 1920, 1080, 60, 8_000_000),
    HD_720P("720p HD • 30fps", "HD", 1280, 720, 30, 4_000_000)
}

enum class OrientationOption(val label: String) {
    PORTRAIT("Portrait"),
    LANDSCAPE("Landscape"),
    AUTO("Auto")
}

enum class CountdownOption(val label: String, val seconds: Int) {
    IMMEDIATE("Immediate (Off)", 0),
    SEC_3("3 Seconds", 3),
    SEC_5("5 Seconds", 5),
    SEC_10("10 Seconds", 10)
}

data class RecordingConfig(
    val audioOption: AudioOption = AudioOption.INTERNAL,
    val qualityOption: QualityOption = QualityOption.UHD_4K,
    val orientationOption: OrientationOption = OrientationOption.PORTRAIT,
    val countdownOption: CountdownOption = CountdownOption.IMMEDIATE,
    val controlsEnabled: Boolean = false,
    val penEnabled: Boolean = false,
    val facecamEnabled: Boolean = false,
    val touchesEnabled: Boolean = false
)
