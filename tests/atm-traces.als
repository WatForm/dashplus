/*
   Automatically created via translation of a Dash model to Alloy
   on 2025-09-05 09:58:50
*/

open util/integer
open util/boolean
open util/traces[DshSnapshot] as DshSnapshot
abstract sig DshStates {}
abstract sig ATM extends DshStates {} 
one sig ATM_Idle extends ATM {} 
one sig ATM_PINVerification extends ATM {} 
one sig ATM_Withdraw extends ATM {} 

abstract sig Transitions {}
one sig ATM_PINVerification_exceededAttempts extends Transitions {} 
one sig ATM_Idle_start extends Transitions {} 
one sig ATM_Withdraw_invalid extends Transitions {} 
one sig ATM_PINVerification_invalid extends Transitions {} 
one sig ATM_Withdraw_valid extends Transitions {} 
one sig ATM_PINVerification_cancel extends Transitions {} 
one sig ATM_Withdraw_cancel extends Transitions {} 
one sig ATM_PINVerification_valid extends Transitions {} 

abstract sig DshEvents {}
abstract sig DshIntEvents extends DshEvents {} 
one sig ATM_eject extends DshIntEvents {} 
abstract sig DshEnvEvents extends DshEvents {} 
one sig ATM_invalidPin extends DshEnvEvents {} 
one sig ATM_validPin extends DshEnvEvents {} 
one sig ATM_insertCard extends DshEnvEvents {} 
one sig ATM_cancel extends DshEnvEvents {} 

sig DshSnapshot {
  dsh_conf0: set DshStates,
  dsh_taken0: set Transitions,
  dsh_events0: set DshEvents,
  ATM_attempts: one Int,
  ATM_balance: one Int,
  ATM_withdrawAmount: one Int
}

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

pred ATM_Idle_start_pre [
	s: DshSnapshot] {
  some (ATM_Idle & (s.dsh_conf0))
  ATM_insertCard in (s.dsh_events0)
}


pred ATM_Idle_start_post [
	s: DshSnapshot,
	sn: DshSnapshot] {
  (sn.dsh_conf0) =
  (((s.dsh_conf0) -
      ((ATM_Idle + ATM_PINVerification) + ATM_Withdraw)) +
     ATM_PINVerification)
  (sn.ATM_attempts) = (3)
  (sn.dsh_taken0) = ATM_Idle_start
  (s.ATM_balance) = (sn.ATM_balance)
}

pred ATM_Idle_start [
	s: DshSnapshot,
	sn: DshSnapshot] {
  s.ATM_Idle_start_pre
  sn.(s.ATM_Idle_start_post)
}

pred ATM_Withdraw_invalid_pre [
	s: DshSnapshot] {
  some (ATM_Withdraw & (s.dsh_conf0))
  s.ATM_withdrawAmount > s.ATM_balance
}


pred ATM_Withdraw_invalid_post [
	s: DshSnapshot,
	sn: DshSnapshot] {
  (sn.dsh_conf0) =
  (((s.dsh_conf0) - ATM_Withdraw) + ATM_Withdraw)
  (sn.dsh_taken0) = ATM_Withdraw_invalid
  (s.ATM_balance) = (sn.ATM_balance)
  (s.ATM_attempts) = (sn.ATM_attempts)
}

pred ATM_Withdraw_invalid [
	s: DshSnapshot,
	sn: DshSnapshot] {
  s.ATM_Withdraw_invalid_pre
  sn.(s.ATM_Withdraw_invalid_post)
}

pred ATM_PINVerification_invalid_pre [
	s: DshSnapshot] {
  some (ATM_PINVerification & (s.dsh_conf0))
  s.ATM_attempts > (0)
  ATM_invalidPin in (s.dsh_events0)
}


pred ATM_PINVerification_invalid_post [
	s: DshSnapshot,
	sn: DshSnapshot] {
  (sn.dsh_conf0) =
  (((s.dsh_conf0) - ATM_PINVerification) +
     ATM_PINVerification)
  (sn.ATM_attempts) = ((1).((s.ATM_attempts).minus))
  (sn.dsh_taken0) = ATM_PINVerification_invalid
  (s.ATM_balance) = (sn.ATM_balance)
}

pred ATM_PINVerification_invalid [
	s: DshSnapshot,
	sn: DshSnapshot] {
  s.ATM_PINVerification_invalid_pre
  sn.(s.ATM_PINVerification_invalid_post)
}

pred ATM_Withdraw_valid_pre [
	s: DshSnapshot] {
  some (ATM_Withdraw & (s.dsh_conf0))
  s.ATM_withdrawAmount <= s.ATM_balance
}


pred ATM_Withdraw_valid_post [
	s: DshSnapshot,
	sn: DshSnapshot] {
  (sn.dsh_conf0) =
  (((s.dsh_conf0) -
      ((ATM_Idle + ATM_PINVerification) + ATM_Withdraw)) +
     ATM_Idle)
  (sn.ATM_balance) =
  ((s.ATM_withdrawAmount).((s.ATM_balance).minus))
  (sn.dsh_taken0) = ATM_Withdraw_valid
  (s.ATM_attempts) = (sn.ATM_attempts)
}

pred ATM_Withdraw_valid [
	s: DshSnapshot,
	sn: DshSnapshot] {
  s.ATM_Withdraw_valid_pre
  sn.(s.ATM_Withdraw_valid_post)
}

pred ATM_PINVerification_cancel_pre [
	s: DshSnapshot] {
  some (ATM_PINVerification & (s.dsh_conf0))
  ATM_cancel in (s.dsh_events0)
}


pred ATM_PINVerification_cancel_post [
	s: DshSnapshot,
	sn: DshSnapshot] {
  (sn.dsh_conf0) =
  (((s.dsh_conf0) -
      ((ATM_Idle + ATM_PINVerification) + ATM_Withdraw)) +
     ATM_Idle)
  (sn.dsh_taken0) = ATM_PINVerification_cancel
  (s.ATM_balance) = (sn.ATM_balance)
  (s.ATM_attempts) = (sn.ATM_attempts)
}

pred ATM_PINVerification_cancel [
	s: DshSnapshot,
	sn: DshSnapshot] {
  s.ATM_PINVerification_cancel_pre
  sn.(s.ATM_PINVerification_cancel_post)
}

pred ATM_Withdraw_cancel_pre [
	s: DshSnapshot] {
  some (ATM_Withdraw & (s.dsh_conf0))
  ATM_cancel in (s.dsh_events0)
}


pred ATM_Withdraw_cancel_post [
	s: DshSnapshot,
	sn: DshSnapshot] {
  (sn.dsh_conf0) =
  (((s.dsh_conf0) -
      ((ATM_Idle + ATM_PINVerification) + ATM_Withdraw)) +
     ATM_Idle)
  (sn.dsh_taken0) = ATM_Withdraw_cancel
  (s.ATM_balance) = (sn.ATM_balance)
  (s.ATM_attempts) = (sn.ATM_attempts)
}

pred ATM_Withdraw_cancel [
	s: DshSnapshot,
	sn: DshSnapshot] {
  s.ATM_Withdraw_cancel_pre
  sn.(s.ATM_Withdraw_cancel_post)
}

pred ATM_PINVerification_valid_pre [
	s: DshSnapshot] {
  some (ATM_PINVerification & (s.dsh_conf0))
  ATM_validPin in (s.dsh_events0)
}


pred ATM_PINVerification_valid_post [
	s: DshSnapshot,
	sn: DshSnapshot] {
  (sn.dsh_conf0) =
  (((s.dsh_conf0) -
      ((ATM_Idle + ATM_PINVerification) + ATM_Withdraw)) +
     ATM_Withdraw)
  (sn.dsh_taken0) = ATM_PINVerification_valid
  (s.ATM_balance) = (sn.ATM_balance)
  (s.ATM_attempts) = (sn.ATM_attempts)
}

pred ATM_PINVerification_valid [
	s: DshSnapshot,
	sn: DshSnapshot] {
  s.ATM_PINVerification_valid_pre
  sn.(s.ATM_PINVerification_valid_post)
}

pred dsh_small_step [
	s: DshSnapshot,
	sn: DshSnapshot] {
  { (sn.(s.ATM_PINVerification_exceededAttempts)) ||
    (sn.(s.ATM_Idle_start)) ||
    (sn.(s.ATM_Withdraw_invalid)) ||
    (sn.(s.ATM_PINVerification_invalid)) ||
    (sn.(s.ATM_Withdraw_valid)) ||
    (sn.(s.ATM_PINVerification_cancel)) ||
    (sn.(s.ATM_Withdraw_cancel)) ||
    (sn.(s.ATM_PINVerification_valid)) ||
    (!({ (s.ATM_PINVerification_exceededAttempts_pre) ||
           (s.ATM_Idle_start_pre) ||
           (s.ATM_Withdraw_invalid_pre) ||
           (s.ATM_PINVerification_invalid_pre) ||
           (s.ATM_Withdraw_valid_pre) ||
           (s.ATM_PINVerification_cancel_pre) ||
           (s.ATM_Withdraw_cancel_pre) ||
           (s.ATM_PINVerification_valid_pre) }) &&
       (sn.(s.dsh_stutter))) }
}

pred dsh_stutter [
	s: DshSnapshot,
	sn: DshSnapshot] {
  (sn.dsh_conf0) = (s.dsh_conf0)
  (sn.dsh_taken0) = none
  ((sn.dsh_events0) :> DshIntEvents) = none
  (sn.ATM_attempts) = (s.ATM_attempts)
  (sn.ATM_balance) = (s.ATM_balance)
}

fact dsh_traces_fact {
  DshSnapshot/first.dsh_initial
  {some
  DshSnapshot/back=>
    ((all s: DshSnapshot | (s.DshSnapshot/next).(s.dsh_small_step)))
  else
    ((all s: DshSnapshot - DshSnapshot/last | (s.DshSnapshot/next).(s.dsh_small_step)))}

}

fact allSnapshotsDifferent {
  (all s: DshSnapshot,sn: DshSnapshot | (((s.dsh_conf0) =
                                          (sn.dsh_conf0)) &&
                                         ((s.dsh_taken0) =
                                            (sn.dsh_taken0)) &&
                                         ((s.dsh_events0) =
                                            (sn.dsh_events0)) &&
                                         ((s.ATM_attempts) =
                                            (sn.ATM_attempts)) &&
                                         ((s.ATM_balance) =
                                            (sn.ATM_balance)) &&
                                         ((s.ATM_withdrawAmount)
                                            =
                                            (sn.ATM_withdrawAmount)))
                                        => (s = sn))
}

pred dsh_strong_no_stutter {
  (all s: DshSnapshot | { (s = DshSnapshot/first) ||
                          !((s.dsh_taken0) = none) })
}

pred dsh_enough_operations {
  (some s: DshSnapshot,sn: DshSnapshot | sn.(s.ATM_PINVerification_exceededAttempts))
  (some s: DshSnapshot,sn: DshSnapshot | sn.(s.ATM_Idle_start))
  (some s: DshSnapshot,sn: DshSnapshot | sn.(s.ATM_Withdraw_invalid))
  (some s: DshSnapshot,sn: DshSnapshot | sn.(s.ATM_PINVerification_invalid))
  (some s: DshSnapshot,sn: DshSnapshot | sn.(s.ATM_Withdraw_valid))
  (some s: DshSnapshot,sn: DshSnapshot | sn.(s.ATM_PINVerification_cancel))
  (some s: DshSnapshot,sn: DshSnapshot | sn.(s.ATM_Withdraw_cancel))
  (some s: DshSnapshot,sn: DshSnapshot | sn.(s.ATM_PINVerification_valid))
}

pred dsh_single_event {
  (all s: DshSnapshot | lone ((s.dsh_events0) :> DshEnvEvents))
}

assert CounterExample1 {
 	all s: DshSnapshot, sn: DshSnapshot | !(sn = s.DshSnapshot/next && s.ATM_attempts > sn.ATM_attempts)
}

check CounterExample1 for 5 but 8 int

