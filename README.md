### Introduction
The purpose of this codebase is to illustrate how to build an Android App that can track a
user's location while the app is in the background. In Android Oreo (API 28) and up, you can't just
run a background service, instead you have to attach a custom notification to the service or Android
will kill your service.

### Architecture
I tried to keep this as simple as possible so that there isn't any unnecessary confusion. The only additional
feature that is implemented is a Firebase Cloud Function that is called to storage the user's location in a
database.

### Video Walk Through


### Which other permissions do I have to have a foreground service?


### License





