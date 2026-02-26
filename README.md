# Gifster – Compose Multiplatform GIF App

Gifster is a **cross-platform GIF application** built using **Kotlin Compose Multiplatform**, supporting **Android and iOS**. It allows users to browse trending GIFs, search GIFs, view GIF details, download, share, and explore random GIFs.  

---

## Features

- **Home Screen**
  - Browse trending GIFs
  - Access random GIF generator
  - Navigate to search screen

- **Search**
  - Debounced search with results
  - Pagination support for large result sets
  - Empty and error state handling

- **GIF Detail Screen**
  - Fullscreen GIF display
  - Title, rating, type, sticker info
  - Download GIF to device
  - Share GIF using native share sheet
  - Uses cached images for performance

- **Random GIF Generator**
  - Confirmation dialog before generating random GIF
  - Fetches from API and navigates to detail view

- **Error Handling**
  - Custom dialogs with retry/dismiss actions
  - Network & image loading errors

---

## Tech Stack

- **Kotlin Compose Multiplatform**
- **Kotlin Coroutines & Flow**
- **Koin for Dependency Injection**
- **Coil / CachedImage for image loading**
- **Ktor HTTP client**

---

## Project Structure

```
composeApp/
├── commonMain/       # Shared business logic, UI components
├── androidMain/      # Android-specific implementations (FileProvider, sharing)
├── iosMain/          # iOS-specific implementations
├── features/
│   ├── home/
│   ├── search/
│   ├── gif_detail/
│   └── random/
└── core/
├── navigation/
├── data/
└── utils/
```

---

## Setup

### 1. Clone Repository

```bash
git clone https://github.com/yourusername/gifster.git
cd gifster
```
### 2. Android
	•	Open in Android Studio Flamingo+
	•	Ensure minSdkVersion >= 26

### 4. iOS
	•	Open iosApp.xcworkspace in Xcode 14+
	•	Configure signing & provisioning
	•	GIF sharing uses UIActivityViewController
	•	Run on simulator or real device

### Dependency Injection
	•	Uses Koin across all platforms
	•	Provides platform-specific implementations for:
	•	GifFileDownloader
	•	GifFileSharer
	•	Platform contexts

### Caching
	•	GIF images are cached in memory and disk
	•	Retry mechanism for failed image loads
	•	Uses AsyncImage + CachePolicy.ENABLED (Coil)

### Navigation
	•	Uses Navigation 3
	•	NavDisplay + Routes sealed class for screens
	•	Supports:
	•	Back stack
	•	Subview composition
	•	Dialog overlays
  
### API Integration
	•	Fetch trending, search, and random GIFs from Giphy API
	•	Pagination support with offset calculation
	•	Network error handling using onSuccess / onError extensions
  
### Contributing
	1.	Fork the repository
	2.	Create your feature branch
	3.	Commit your changes
	4.	Push and create a pull request
