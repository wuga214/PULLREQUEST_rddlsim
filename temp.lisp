domain hvac_vav_fix {
  requirements = {
  };
  types {
    space : object;
  };
  pvariables {
    TEMP(space) : {state-fluent, real, default = 10.0};
    R_OUTSIDE(space) : {non-fluent, real, default = 4};
    ADJ(space, space) : {non-fluent, bool, default = false};
    TEMP_UP(space) : {non-fluent, real, default = 23.5};
    PENALTY : {non-fluent, real, default = 20000};
    TEMP_OUTSIDE(space) : {non-fluent, real, default = 6.0};
    IS_ROOM(space) : {non-fluent, bool, default = false};
    TEMP_AIR : {non-fluent, real, default = 40};
    AIR(space) : {action-fluent, real, default = 0.0};
    COST_AIR : {non-fluent, real, default = 1};
    R_WALL(space, space) : {non-fluent, real, default = 1.5};
    TEMP_HALL(space) : {non-fluent, real, default = 10.0};
    CAP(space) : {non-fluent, real, default = 80};
    AIR_MAX(space) : {non-fluent, real, default = 10.0};
    R_HALL(space) : {non-fluent, real, default = 2};
    CAP_AIR : {non-fluent, real, default = 1.006};
    TEMP_LOW(space) : {non-fluent, real, default = 20.0};
    ADJ_OUTSIDE(space) : {non-fluent, bool, default = false};
    TIME_DELTA : {non-fluent, real, default = 1};
    ADJ_HALL(space) : {non-fluent, bool, default = false};
  };
  cpfs {
    (TEMP' ?s) = (+ (TEMP ?s) (* (/ (TIME_DELTA) (CAP ?s)) (+ (+ (+ (* (* (AIR ?s) (CAP_AIR)) (- (TEMP_AIR) (TEMP ?s))) (sum ( (?p : space) ) (/ (* (| (ADJ ?s ?p) (ADJ ?p ?s) ) (- (TEMP ?p) (TEMP ?s))) (R_WALL ?s ?p)))) (/ (* (ADJ_OUTSIDE ?s) (- (TEMP_OUTSIDE ?s) (TEMP ?s))) (R_OUTSIDE ?s))) (/ (* (ADJ_HALL ?s) (- (TEMP_HALL ?s) (TEMP ?s))) (R_HALL ?s)))));
  };
  reward = (- 0 (sum ( (?s : space) ) (+ (+ (* (AIR ?s) (COST_AIR)) (* (| (< (TEMP ?s) (TEMP_LOW ?s)) (> (TEMP ?s) (TEMP_UP ?s)) ) (PENALTY))) (* 10.0 (abs (- (/ (+ (TEMP_UP ?s) (TEMP_LOW ?s)) 2.0) (TEMP ?s)))))));
  action-preconditions {
    (forall ( (?s : space) ) (>= (AIR ?s) 0));
    (forall ( (?s : space) ) (<= (AIR ?s) (AIR_MAX ?s)));
  };
}

non-fluents nf_hvac_vav_fix {
  domain = hvac_vav_fix;
  objects {
    space : {r1, r2, r3, r4, r5, r6};
  };
  non-fluents {
    IS_ROOM(r1);
    IS_ROOM(r2);
    IS_ROOM(r3);
    IS_ROOM(r4);
    IS_ROOM(r5);
    IS_ROOM(r6);
    ADJ(r1, r2);
    ADJ(r1, r4);
    ADJ(r2, r3);
    ADJ(r2, r5);
    ADJ(r3, r6);
    ADJ(r4, r5);
    ADJ(r5, r6);
    ADJ_OUTSIDE(r1);
    ADJ_OUTSIDE(r3);
    ADJ_OUTSIDE(r4);
    ADJ_OUTSIDE(r6);
    ADJ_HALL(r1);
    ADJ_HALL(r2);
    ADJ_HALL(r3);
    ADJ_HALL(r4);
    ADJ_HALL(r5);
    ADJ_HALL(r6);
  };
}

instance inst_hvac_vav_fix {
  domain = hvac_vav_fix;
  non-fluents = nf_hvac_vav_fix;
  max-nondef-actions = pos-inf;
  horizon = 10;
  discount = 1.0;
}


