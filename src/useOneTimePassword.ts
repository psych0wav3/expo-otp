/* eslint-disable prettier/prettier */
import { useEffect, useState } from "react";
import { Platform } from "react-native";

import { getHash, removeListener, startAndListener } from ".";
import { IUseOneTimePassword } from "./ExpoOtp.types";

export const useOneTimePassword = ({ numberOfDigits }: IUseOneTimePassword) => {
  // Constants
  const IsAndroid = Platform.OS === "android";

  // States
  const [error, setError] = useState<boolean>(false);
  const [message, setMessage] = useState<string | null>(null);
  const [otp, setOtp] = useState<string | null>(null);
  const [hash, setHash] = useState<string[] | null>([]);

  // Effects
  useEffect(() => {
    if (!IsAndroid) {
      return;
    }
    setHash(getHash);
    (async () => await startAndListener(handleMessageReceive))();
    return () => {
      removeListener();
    };
  }, []);

  // Handlers
  const handleMessageReceive = (incomeMsg) => {
    if (incomeMsg.message === "Timeout Error.") {
      setError(true);
    } else {
      setMessage(incomeMsg.message);
      if (numberOfDigits && incomeMsg.message) {
        const otpDigits = new RegExp(`(\\d{${numberOfDigits}})`, "g").exec(
          incomeMsg.message,
        );
        if (otpDigits && otpDigits[1]) setOtp(otpDigits[1]);
      }
    }
  };

  //Methods
  const startListener = () => {
    setOtp("");
    setMessage("");
    startAndListener(handleMessageReceive);
  };

  const stopListener = () => {
    removeListener();
  };

  return { otp, message, hash, error, stopListener, startListener };
};
