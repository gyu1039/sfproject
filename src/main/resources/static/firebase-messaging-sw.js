// Initialize Firebase
// 이 js파일은 반드시 static의 root에 있어야함. 마찬가지로 컨텍스트 경로도 /firebase-messaging-sw.js여야함.

importScripts('https://www.gstatic.com/firebasejs/6.5.0/firebase-app.js');
importScripts('https://www.gstatic.com/firebasejs/6.5.0/firebase-messaging.js');


// Initialize the Firebase app in the service worker by passing in
// your app's Firebase config object.
// https://firebase.google.com/docs/web/setup#config-object
//const firebaseApp =
firebase.initializeApp({
    // 설정 정보 apiKey, authDomain, projectId, storageBucket messagingSenderId, appId, measurementId

});

// Retrieve an instance of Firebase Messaging so that it can handle background
// messages.
//아래 두 코드는 동일한 기능을 한다. 둘 중 하나만 주석을 풀자.
const messaging = firebase.messaging()
//const messaging = getMessaging(firebaseApp);