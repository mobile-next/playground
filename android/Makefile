APK_SRC  = app/build/outputs/apk/debug/app-debug.apk
VERSION ?= dev
APK_OUT  = Playground-$(VERSION).apk

.PHONY: all clean

all: $(APK_OUT)

$(APK_OUT):
	./gradlew assembleDebug
	cp $(APK_SRC) $(APK_OUT)

clean:
	./gradlew clean
	rm -f Playground-*.apk
