# SoundWallpaper

A minimal Android live wallpaper app that imports a local video through the system file picker and sets it as a live wallpaper. It loops the video and plays audio while the wallpaper is visible, and pauses when the wallpaper is not visible or the lock screen is active.

## Limitations

- Android and OEM launchers can manage wallpaper visibility differently, so pausing when another app covers the home screen is best-effort, not guaranteed on every Realme build.
- Lock-screen audio is intentionally paused.
- This app copies the chosen video into private app storage, so no broad storage permission is required.
