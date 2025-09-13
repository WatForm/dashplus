pred dsh_initial [
	s: DshSnapshot] {
  ((s.dsh_conf0) = ATM_Idle) &&
  ((s.dsh_taken0) = none) &&
  (((s.dsh_events0) :> DshIntEvents) = none) &&
  ((s.ATM_attempts) = (3)) &&
  (s.ATM_balance > (0))
}

fact inv {
  (all s: DshSnapshot | s.ATM_withdrawAmount > (0))
}

pred ATM_PINVerification_exceededAttempts_pre [
	s: DshSnapshot] {
  some (ATM_PINVerification & (s.dsh_conf0))
  s.ATM_attempts <= (0)
}


pred ATM_PINVerification_exceededAttempts_post [
	s: DshSnapshot,
	sn: DshSnapshot] {
  (sn.dsh_conf0) =
  (((s.dsh_conf0) -
      ((ATM_Idle + ATM_PINVerification) + ATM_Withdraw)) +
     ATM_Idle)
  (sn.dsh_taken0) = ATM_PINVerification_exceededAttempts
  (s.ATM_balance) = (sn.ATM_balance)
  (s.ATM_attempts) = (sn.ATM_attempts)
}

pred ATM_PINVerification_exceededAttempts [
	s: DshSnapshot,
	sn: DshSnapshot] {
  s.ATM_PINVerification_exceededAttempts_pre
  sn.(s.ATM_PINVerification_exceededAttempts_post)
}

