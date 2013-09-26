# Streamly RSS Reader
> A river style RSS reader for Android.

> River style RSS reader displays all the current feed entries for the blogs you subscribe to.  This differs from a *"read and mark"* style rss reader which tracks which entries you've read.

> The intent of this is to give you an *"at a glance"* look at what's going on with the blogs you follow.

# Features

* Support for Android 2.3 and above

* Add blogs to follow

* Phone and tablet support with a UI optimized to utilize space better on larger screens.

* Share blog posts as you read.

* Copy a blog post url to the clipboard.

* Open a blog post in your default browser.

## Setup
> Update the v7 appcompat project

	$ android update project -p /path/to/Android/android-sdk/extras/android/support/v7/appcompat/ -t android-18

> Add the path to your Android SDK to your local.properties file

	$ echo "sdk.dir=/path/to/Android/android-sdk/" >> local.properties

> Create a symbolic link to the v7 appcompat library project
> *NOTE: MUST BE RELATIVE PATH*

	# *nix (OS X, Linux, Unix)
	$ ln -s relative/path/to/Android/android-sdk/extras/android/support/v7/appcompat/ v7appcompat

	# Windows
	> C:\mklink \D v7appcompat C:\Path\To\Android\android-sdk\extras\android\support\v7\appcompat
