
SELECT  g.* FROM pm.user AS u, pm.user_group AS ug, pm.group AS g
  WHERE ug.user_id = u.id and u.id=1 and g.id=ug.group_id;