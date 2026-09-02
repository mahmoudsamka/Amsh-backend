# Amsh Android app bootstrap

This branch adds a Kotlin-based Android app bootstrap with:

- LoginActivity and MainActivity implementations
- Retrofit network client (ApiClient & ApiService)
- RecyclerView adapter for items
- Basic Gradle files (project & app module)
- strings.xml resource
- Manifest configured with package name

What you must do after merging:

1. Update the Retrofit BASE_URL in ApiClient.kt to point to your backend.
2. Verify the API endpoints in ApiService match your backend paths / request/response shapes.
3. Optionally migrate Gradle Kotlin plugin versions to match your CI settings.

To build locally:
- Open the project in Android Studio, let it sync Gradle, and then run the app on a device or emulator.
