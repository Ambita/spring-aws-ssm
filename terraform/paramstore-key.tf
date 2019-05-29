resource "aws_kms_key" "spring_aws_ssm" {
  description         = "Spring AWS SSM itest"
  enable_key_rotation = true

  tags = {
    Name = "Spring AWS SSM itest"
  }
}

resource "aws_kms_alias" "paramstore" {
  name          = "alias/spring-aws-ssm-itest"
  target_key_id = "${aws_kms_key.spring_aws_ssm.key_id}"
}


