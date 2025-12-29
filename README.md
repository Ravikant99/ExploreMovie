# ExploreMovie 🎬

A modern Android movie exploration app built with Jetpack Compose and Material Design 3 and TMDB Api.

## Features ✨

### 🏠 Home Screen
- Beautiful carousel showcasing trending movies
- Browse by genres
- Discover popular, top-rated, and upcoming movies
- View trending actors and personalities
- Shimmer loading effects for better UX

### 🔍 Search
- Real-time movie search
- Category filters
- Grid layout for search results

### 🎬 Movie Details
- Comprehensive movie information
- Cast and crew details
- Play trailers (YouTube integration)
- View similar movies
- Ratings and reviews

### 📥 Downloads
- View local video files
- Video player with playlist support
- Thumbnail previews
- Next/Previous navigation

### 👤 Profile
- User profile management
- Statistics (Watched, Downloads, Favorites)
- Settings and preferences
- Account management

### 🎥 Video Player
- ExoPlayer integration
- Portrait and landscape modes
- Playlist support with next/previous
- Audio focus management
- Custom controls
- Proper lifecycle handling

## Tech Stack 🛠️

- **Language**: Kotlin
- **UI Framework**: Jetpack Compose
- **Architecture**: MVVM (Model-View-ViewModel)
- **Navigation**: Jetpack Navigation Compose
- **Networking**: Retrofit, OkHttp
- **Image Loading**: Coil, Glide
- **Video Playback**: ExoPlayer (Media3)
- **Async Operations**: Coroutines, Flow
- **Dependency Injection**: Manual DI (ViewModel factories)
- **API**: TMDB (The Movie Database)

## Architecture 🏗️

```
app/
├── screens/          # UI screens (Composables)
│   ├── home/        # Home screen with ViewModels
│   ├── details/     # Movie details and see more
│   ├── search/      # Search functionality
│   ├── downloads/   # Local video downloads
│   ├── profile/     # User profile
│   └── player/      # Video player (ExoPlayer & YouTube)
├── ui/
│   ├── composableItems/  # Reusable UI components
│   └── theme/           # App theme and colors
├── common/          # Shared models and utilities
├── webServices/     # API clients and services
├── navigation/      # Navigation graph
└── utils/          # Utility classes
```

## Key Features Implementation 🔑

### State Management
- StateFlow and LiveData for reactive UI
- ViewModel survives configuration changes
- Proper lifecycle handling

### Navigation
- Type-safe navigation with sealed classes
- Deep linking support
- Back stack management

### Caching & Performance
- Image caching with Coil/Glide
- Data caching in ViewModels
- Single data fetch per activity lifecycle
- Scroll position preservation

### Video Player
- Single ExoPlayer instance guarantee
- Thread-safe operations
- Null safety throughout
- Exception handling
- Audio focus management
- Comprehensive logging

## Setup 🚀

### Prerequisites
- Android Studio Hedgehog or later
- Minimum SDK: 24 (Android 7.0)
- Target SDK: 34 (Android 14)
- Kotlin 1.9+

### API Configuration
1. Get your TMDB API key from [themoviedb.org](https://www.themoviedb.org/settings/api)
2. Add to `local.properties`:
```properties
tmdb.api.key=your_api_key_here
```

### Build & Run
```bash
./gradlew assembleDebug
./gradlew installDebug
```

## Permissions Required 📱

```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.READ_MEDIA_VIDEO" /> <!-- Android 13+ -->
```

## Screenshots 📸

[Add your screenshots here]

## Project Structure 📂

### Main Packages

**screens/** - All UI screens as Composables
- Each screen has its own package
- ViewModel for business logic
- Screen composable for UI

**ui/composableItems/** - Reusable UI components
- MovieCard, StarCastItem, ShimmerEffect, etc.
- BottomNavigationBar
- Common UI elements

**common/** - Shared data models
- Movie, Person, Genre models
- Response wrappers

**webServices/** - Network layer
- API interfaces
- Repository pattern
- Resource wrapper for API states

## Development Guidelines 📋

### Code Style
- Follow Kotlin coding conventions
- Use Jetpack Compose best practices
- MVVM architecture pattern
- Proper error handling and null safety

### Commit Messages
- Use conventional commits format
- Examples:
  - `feat: Add profile screen`
  - `fix: Resolve player lifecycle issue`
  - `refactor: Clean up home screen code`

## Known Limitations ⚠️

- YouTube player may not work for restricted videos (redirects to YouTube app)
- Video thumbnails may not load for all devices
- Search is limited to movies only (no TV shows)

## Future Enhancements 🚀

- [ ] TV Shows support
- [ ] Watchlist functionality
- [ ] User authentication
- [ ] Offline mode
- [ ] Dark/Light theme toggle
- [ ] Multiple language support
- [ ] Advanced filters

## Contributing 🤝

Contributions are welcome! Please feel free to submit a Pull Request.

## License 📄

This project is open source and available under the [MIT License](LICENSE).

## Credits 🙏

- **TMDB API** - Movie data and images
- **Material Design** - UI design system
- **Jetpack Compose** - Modern Android UI toolkit

## Contact 📧

For questions or feedback, please reach out to:
- Email: mail2ravikant99@gmail.com
- GitHub: [@ravikant99](https://github.com/ravikant99)

---

**Note**: This app uses TMDB API but is not endorsed or certified by TMDB.

