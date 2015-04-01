redis = {}
redisObj = redisObj or {}

function redis.call(command, ...)
  local args = table.pack(...)
  if command == 'set' then
    if args.n == 2 then
      return redisObj:set(args[0], args[1], {})
    end
  end
end

function redis.pcall(command, ...)
  return pcall(redis.call(command, ...))
end