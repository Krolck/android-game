Build another screen with a shop that lets me spend money (instead of clicks) to get upgrades.
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