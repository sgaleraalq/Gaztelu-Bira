# Firestore maps documents onto these classes by reflection (toObject / set), so R8 must keep
# their names, no-arg constructors and getters. Includes nested classes such as MatchResponseScore$Score.
-keep class com.sgale.gaztelubira.core.network.firebase.response.** { *; }
