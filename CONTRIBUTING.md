# Contributing to ExploreMovie

First off, thank you for considering contributing to ExploreMovie! 🎉

## Code of Conduct

- Be respectful and inclusive
- Provide constructive feedback
- Focus on what is best for the community

## How Can I Contribute?

### Reporting Bugs 🐛

Before creating bug reports, please check existing issues. When creating a bug report, include:

- **Clear title and description**
- **Steps to reproduce**
- **Expected vs actual behavior**
- **Screenshots** (if applicable)
- **Device info** (OS version, device model)
- **Logs** (if relevant)

### Suggesting Enhancements 💡

Enhancement suggestions are tracked as GitHub issues. When creating an enhancement suggestion:

- **Use a clear and descriptive title**
- **Provide detailed description** of the suggested enhancement
- **Explain why** this enhancement would be useful
- **List alternatives** you've considered

### Pull Requests 🔧

1. **Fork** the repository
2. **Create a branch** from `main`:
   ```bash
   git checkout -b feature/your-feature-name
   ```
3. **Make your changes**
4. **Test thoroughly**
5. **Commit** with clear messages:
   ```bash
   git commit -m "feat: Add amazing feature"
   ```
6. **Push** to your fork:
   ```bash
   git push origin feature/your-feature-name
   ```
7. **Open a Pull Request**

## Development Setup

### Prerequisites
- Android Studio Hedgehog or later
- JDK 11 or higher
- TMDB API key

### Setup Steps
1. Clone the repository
2. Add your TMDB API key to `local.properties`:
   ```properties
   tmdb.api.key=your_api_key_here
   ```
3. Sync project with Gradle files
4. Run on emulator or device

## Coding Guidelines

### Kotlin Style Guide
- Follow [Kotlin coding conventions](https://kotlinlang.org/docs/coding-conventions.html)
- Use meaningful variable and function names
- Keep functions small and focused
- Add comments for complex logic

### Compose Best Practices
- Extract reusable composables
- Use `remember` for state management
- Hoist state when needed
- Follow Material Design 3 guidelines

### Architecture
- Follow MVVM pattern
- Keep ViewModels free of Android framework dependencies
- Use repositories for data operations
- Handle loading/error states properly

### Git Commit Messages
Use conventional commits format:

- `feat:` New feature
- `fix:` Bug fix
- `refactor:` Code restructuring
- `docs:` Documentation changes
- `style:` Formatting changes
- `test:` Adding tests
- `chore:` Maintenance tasks

Examples:
```
feat: Add watchlist functionality
fix: Resolve player crash on rotation
refactor: Improve home screen performance
docs: Update setup instructions
```

## Testing

### Before Submitting PR
- [ ] Test on multiple screen sizes
- [ ] Test portrait and landscape modes
- [ ] Test with slow network
- [ ] Test with no network
- [ ] Check for memory leaks
- [ ] Verify no new lint warnings
- [ ] Ensure all existing tests pass

### Writing Tests
- Add unit tests for ViewModels
- Add UI tests for critical flows
- Mock external dependencies

## Code Review Process

1. **Automated checks** must pass (build, lint)
2. **Manual review** by maintainers
3. **Feedback** will be provided
4. **Approval** needed before merge

## Project Structure

```
app/
├── screens/           # UI screens
│   ├── home/         # Home screen + ViewModel
│   ├── details/      # Details + SeeMore screens
│   ├── search/       # Search functionality
│   ├── downloads/    # Local videos
│   ├── profile/      # User profile
│   └── player/       # Video players
├── ui/
│   ├── composableItems/  # Reusable components
│   └── theme/           # Theming
├── common/           # Shared models
├── webServices/      # API layer
├── navigation/       # Navigation setup
└── utils/           # Utilities
```

## Areas for Contribution

### High Priority
- [ ] Add unit tests
- [ ] Improve error handling
- [ ] Performance optimizations
- [ ] Accessibility improvements

### Features
- [ ] Watchlist functionality
- [ ] User authentication
- [ ] TV Shows support
- [ ] Offline mode
- [ ] Theme customization

### UI/UX
- [ ] Animation improvements
- [ ] Better loading states
- [ ] Enhanced error messages
- [ ] Accessibility features

## Questions?

Feel free to:
- Open an issue
- Start a discussion
- Reach out to maintainers

## Recognition

Contributors will be:
- Listed in README
- Credited in release notes
- Appreciated in the community

Thank you for contributing! 🙌

