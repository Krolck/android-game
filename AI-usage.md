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


Why Modern Android Prefers Single-Activity (No Intents for internal UI)
Android moved away from multi-Activity apps toward Single-Activity with Jetpack Compose because:
•
Performance: Switching between Composable functions in memory is instant, whereas creating a new OS Activity with Intents is heavier and slower.
•
Shared State: Sharing state (like your money balance) via GameViewModel is seamless, whereas Intents require converting data into Bundle extras.


Verified: Built using :app:assembleDebug — the project compiles cleanly with zero errors

