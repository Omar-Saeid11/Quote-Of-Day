# Quote of the Day App

This Android application displays random quotes and allows users to bookmark their favorite quotes.

## Features

- Display a random quote on the main screen.
- Bookmark quotes to view them later in the bookmark section.
- Share quotes with others.
- Offline support using Room database for storing bookmarked quotes.
- Notifications for the quote of the day using WorkManager.

## Screenshots

![WhatsApp Image 2024-07-09 at 5 00 33 AM](https://github.com/Omar-Saeid11/Quote-Of-Day/assets/93147693/3dc5238b-7bb9-498f-875a-31658ff1ffae)
![WhatsApp Image 2024-07-09 at 5 00 33 AM (1)](https://github.com/Omar-Saeid11/Quote-Of-Day/assets/93147693/99ac9bbb-b2bf-4a72-a271-e3e8085fb1e2)
![WhatsApp Image 2024-07-09 at 5 00 33 AM (2)](https://github.com/Omar-Saeid11/Quote-Of-Day/assets/93147693/33f8486a-4467-45d7-b509-139f1860430f)
![WhatsApp Image 2024-07-09 at 5 00 33 AM (3)](https://github.com/Omar-Saeid11/Quote-Of-Day/assets/93147693/479664e0-941c-441c-957b-d2aaa3bb76b6)
![WhatsApp Image 2024-07-09 at 5 00 33 AM (4)](https://github.com/Omar-Saeid11/Quote-Of-Day/assets/93147693/2258a784-8b22-4a50-8f15-b11c797b1bc3)
![WhatsApp Image 2024-07-09 at 5 00 33 AM (5)](https://github.com/Omar-Saeid11/Quote-Of-Day/assets/93147693/b8828bb7-144a-4872-8ae3-eec55a8a5c66)

## Libraries Used

- Android Architecture Components (ViewModel, Room)
- Retrofit for networking
- Kotlin Coroutines for asynchronous programming
- Dagger Hilt for dependency injection
- Navigation Components for navigation within the app
- Material Design Components for UI elements
- WorkManager for background tasks, such as periodic notifications

## Permissions

- INTERNET: Used to fetch quotes from an online API.
- ACCESS_NETWORK_STATE: Check if the device is connected to the internet.
- POST_NOTIFICATIONS: Used to show notifications for the quote of the day.

## Setup

To run this project locally, follow these steps:

1. Clone the repository.
2. Open the project in Android Studio.
3. Connect your Android device or use an emulator.
4. Build and run the project.

## Build Configuration

This project uses Kotlin and follows modern Android development practices:

- Kotlin 1.5.21
- Android Gradle Plugin 7.0.2
- Gradle 7.0.2

## How to Contribute

Contributions to improve the app are welcome! If you have any suggestions, enhancements, or bug fixes, please:

1. Fork the repository.
2. Create your feature branch (`git checkout -b feature/AmazingFeature`).
3. Commit your changes (`git commit -am 'Add some feature'`).
4. Push to the branch (`git push origin feature/AmazingFeature`).
5. Open a pull request.

## Repository

Find the repository [here](https://github.com/Omar-Saeid11/Quote-Of-Day.git).


## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
