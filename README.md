# AlJasoosGame - Imposter Game

An Android game built with Jetpack Compose and Firebase AI.

## Overview

AlJasoosGame (The Spy Game) is an Android application that challenges players to identify the imposter among them. The game uses Google's Gemini AI API for server-side intelligence.

## Tech Stack

- **Language:** Kotlin
- **UI Framework:** Jetpack Compose (Material 3)
- **Architecture:** Android with Compose
- **Backend:** Firebase (AI, App Check)
- **Networking:** Retrofit + OkHttp + Moshi
- **Database:** Room
- **Build:** Gradle with Kotlin DSL + Version Catalog

## Requirements

- Android SDK 24+
- Android Studio (recommended for development)
- Gradle 8.12+
- JDK 17+

## Build

```bash
./gradlew assembleDebug
```

## Environment Variables

Copy `.env.example` to `.env` and configure:

```
GEMINI_API_KEY=your_gemini_api_key
```
