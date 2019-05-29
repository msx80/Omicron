Android building setup.

You should just need to edit omicron.properties file, customizing the values, then run:

gradlew clean android:installDebug android:run

with your phone connected and BAM, you should have your app on the phone.
Current values are for the demo app, you can take a look at file omicron.properties.alienbuster for a complete (still demo) game