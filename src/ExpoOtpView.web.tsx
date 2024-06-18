import * as React from 'react';

import { ExpoOtpViewProps } from './ExpoOtp.types';

export default function ExpoOtpView(props: ExpoOtpViewProps) {
  return (
    <div>
      <span>{props.name}</span>
    </div>
  );
}
