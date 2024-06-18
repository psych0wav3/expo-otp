import { useOneTimePassword } from "expo-otp";
import { useEffect } from "react";
import { StyleSheet, Text, View } from "react-native";

export default function App() {
  const { otp, startListener, stopListener, hash } = useOneTimePassword({
    numberOfDigits: 6,
  });

  useEffect(() => {
    startListener();

    return () => {
      stopListener();
    };
  }, []);

  return (
    <View style={styles.container}>
      <Text>{otp || "empty"}</Text>
      <Text>{hash || "empty"}</Text>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: "#fff",
    alignItems: "center",
    justifyContent: "center",
  },
});
