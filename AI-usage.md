Asked: Build another screen with a shop that lets me spend money (instead of clicks) to get upgrades.
Do not add saving, only the other screen and upgrades.


I have implemented the Shop screen and Money economy with upgrades!
Summary of Changes
1.
Game Economy & ViewModel ( GameViewModel.kt):
◦
Money balance: Earn money instead of simple clicks.
◦
Upgrades List: Built a system for purchasing both Click Upgrades (increases money earned per click) and Passive Upgrades (earns money automatically every second).
◦
Dynamic Pricing: Upgrade costs scale with each level purchased.
2.
Upgrades Model ( Upgrade.kt):
◦
Data model tracking upgrade level, base cost, click bonus, and passive income bonus.
3.
Shop Screen ( ShopScreen.kt):
◦
Header displaying current Money balance.
◦
List of available upgrades (Better Mouse, Piggy Bank, Golden Touch, Money Machine, Diamond Cursor, Crypto Rig).
◦
Buy buttons that automatically enable/disable based on whether you have enough money.
4.
Game Screen ( GameScreen.kt):
◦
Main game screen displaying Total Money, Money Per Click, and Passive Rate ($/s).
◦
Interactive TAP! button to earn money.
5.
Navigation & Passive Income Loop ( MainActivity.kt):
◦
Bottom navigation bar with Game 🎮 and Shop 🛒 tabs.
◦
Background coroutine loop (LaunchedEffect) that automatically credits passive earnings every second.


Fixed: docs say "An Intent is a messaging object you can use to request an action from another app component."

___
Why Modern Android Prefers Single-Activity (No Intents for internal UI)
Android moved away from multi-Activity apps toward Single-Activity with Jetpack Compose because:
•
Performance: Switching between Composable functions in memory is instant, whereas creating a new OS Activity with Intents is heavier and slower.
•
Shared State: Sharing state (like your money balance) via GameViewModel is seamless, whereas Intents require converting data into Bundle extras.


Verified: Built using :app:assembleDebug — the project compiles cleanly with zero errors

___


Asked: Build a sign up screen. You should be able to switch between signing up and logging in. Do not implement saving data yet.

Output: Built Components & Files
1.
Sign Up Layout ( activity_signup.xml):
◦
Input Fields: Full Name (etFullName), Email/Username (etEmail), Password (etPassword), and Confirm Password (etConfirmPassword).
◦
Buttons & Links: SIGN UP button (btnSignUp) and a "Already have an account? Log In" link (tvGoToLogin).
2.
Sign Up Activity ( SignUpActivity.kt):
◦
Validates that all fields are filled and that password matches confirm password.
◦
On successful validation, passes the name to HomeActivity (without persisting/saving data).
◦
On tapping "Already have an account? Log In", switches back to LoginActivity.
3.
Login Screen Updates:
◦
Layout ( activity_login.xml): Added a "Don't have an account? Sign Up" link (tvGoToSignUp) at the bottom.
◦
Activity ( LoginActivity.kt): Added event handler on tvGoToSignUp to launch SignUpActivity.
4.
Manifest Entry ( AndroidManifest.xml):
◦
Registered SignUpActivity.

Verified: Ran code; signed up and login in with invalid and valid credentials

Fixed: Remove email requiring just username

---
I just set up a firebase using firestore. Implement saving data under an "Account" collection. Store username, password, and game upgrade data, including its level. Load the user's game data when logging in.

Acceptance criteria:
Data should be in the database by the time the user logs out. This can be done peridoically or event based.

The user's game data is loaded when logging in.

The user can log in to their account using username and password and handles incorrect credentials.

Out of scope:
Hashing

Output: The Firebase Firestore integration for user accounts and game data persistence has been implemented!
Key Implementations
1. Firestore Database Collection ("Account")
   Every user account is stored as a document under the "Account" collection using their username as the document ID (Account/{username}):
   •
   username: String
   •
   password: String (raw password as requested; hashing out of scope)
   •
   money: Int (user's saved game balance)
   •
   upgrades: Map<String, Int> (mapping each upgrade ID to its current level, e.g. "click_1" ➔ 2)
2. Login & Data Loading ( LoginActivity.kt)
   When logging in:
1.
Queries Account/{username} in Firestore.
2.
Credential Validation:
◦
If the username document doesn't exist ➔ Shows "Username not found."
◦
If the password doesn't match ➔ Shows "Incorrect password."
3.
Data Loading: On successful credential match, reads the user's saved money balance and upgrades level map from Firestore and loads them into GameManager.
3. Account Creation ( SignUpActivity.kt)
   When signing up:
1.
Checks if the username is already taken in the "Account" collection ("Username already taken.").
2.
If available, initializes a new document in "Account" with money = 0 and all upgrade levels set to 0.
4. Persistence & Save on Logout ( GameManager.kt &  HomeActivity.kt)
   •
   Save on Logout: Tapping LOG OUT on HomeActivity triggers GameManager.saveUserDataToFirestore(...), guaranteeing data is saved in the database before returning to the Login screen.
   •
   Auto-Save: Automatically syncs game progress to Firestore periodically and whenever an upgrade is purchased.

Verified: Ran app, signed up, upgraded, and logged out and back in again

Fixed: permission denied error, rules