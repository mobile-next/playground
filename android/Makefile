APK_SRC = app/build/outputs/apk/debug/app-debug.apk
APK_OUT = Playground.apk

.PHONY: all clean

all: $(APK_OUT)

$(APK_OUT):
	./gradlew assembleDebug
	cp $(APK_SRC) $(APK_OUT)

clean:
	./gradlew clean
	rm -f $(APK_OUT)
